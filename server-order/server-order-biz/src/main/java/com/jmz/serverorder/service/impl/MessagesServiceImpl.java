package com.jmz.serverorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverwebsocket.feign.WebSocketFeignClient;
import com.jmz.serverorder.dto.MessagesQueryDTO;
import com.jmz.serverorder.dto.SendMessageDTO;
import com.jmz.serverorder.entity.Message;
import com.jmz.serverorder.mapper.MessageMapper;
import com.jmz.serverorder.service.MessageServices;
import com.jmz.serverorder.vo.ChatMessageVO;
import com.jmz.serverorder.vo.MessageListVO;
import com.jmz.serverorder.vo.SendMessageResponseVO;
import com.jmz.serverorder.vo.UnreadCountVO;
import com.jmz.serveruser.feign.UserFeignClient;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.serveruser.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessagesServiceImpl implements MessageServices {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private WebSocketFeignClient webSocketFeignClient;

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.jmz.jmzcommonsecuritydomain.domain.LoginUser) {
            return ((com.jmz.jmzcommonsecuritydomain.domain.LoginUser) principal).getUserId();
        }
        throw new RuntimeException("无法获取当前登录用户ID");
    }

    private UserInfoVo convertMapToUserInfoVo(Map<String, Object> userMap) {
        if (userMap == null) return null;

        UserInfoVo user = new UserInfoVo();
        user.setUserId(Long.valueOf(userMap.get("userId").toString()));
        user.setUsername((String) userMap.get("username"));
        user.setAvatar((String) userMap.get("avatar"));
        user.setPhone((String) userMap.get("phone"));
        user.setEmail((String) userMap.get("email"));
        user.setNickname((String) userMap.get("nickname"));
        if (userMap.get("gender") != null) {
            user.setGender(Integer.valueOf(userMap.get("gender").toString()));
        }
        if (userMap.get("status") != null) {
            user.setStatus(Integer.valueOf(userMap.get("status").toString()));
        }
        return user;
    }

    @Override
    public Map<String, Object> getMessageList(MessagesQueryDTO queryDTO) {
        Long currentUserId = getCurrentUserId();

        // 获取当前用户的所有消息，按发送者分组，取最后一条
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiverId, currentUserId);
        if (queryDTO.getType() != null) {
            wrapper.eq(Message::getMessageType, queryDTO.getType());
        }
        wrapper.orderByDesc(Message::getCreatedAt);

        List<Message> allMessages = messageMapper.selectList(wrapper);

        // 按发送者分组，取最后一条消息
        Map<Long, Message> lastMessageMap = new HashMap<>();
        for (Message message : allMessages) {
            Long senderId = message.getSenderId();
            if (!lastMessageMap.containsKey(senderId) ||
                message.getCreatedAt().after(lastMessageMap.get(senderId).getCreatedAt())) {
                lastMessageMap.put(senderId, message);
            }
        }

        // 获取发送者信息
        Set<Long> senderIds = lastMessageMap.keySet();
        Map<Long, UserInfoVo> userInfoMap = new HashMap<>();

        for (Long senderId : senderIds) {
            try {
                R feignResult = userFeignClient.getUserById(senderId);
                if (feignResult.isSuccess()) {
                    Object data = feignResult.get(R.DATA_TAG);
                    if (data instanceof Map) {
                        UserInfoVo user = convertMapToUserInfoVo((Map<String, Object>) data);
                        if (user != null) userInfoMap.put(senderId, user);
                    }
                }
            } catch (Exception e) {
                log.error("获取用户信息失败，senderId: {}, error: {}", senderId, e.getMessage());
            }
        }

        // 转换为VO
        List<MessageListVO> messageList = lastMessageMap.values().stream()
            .map(message -> {
                MessageListVO vo = new MessageListVO();
                vo.setId(message.getId());
                vo.setSenderId(message.getSenderId());
                vo.setSenderType(message.getSenderType());
                vo.setContent(message.getContent());
                vo.setMessageType(message.getMessageType());
                vo.setIsRead(message.getIsRead() == 1);
                vo.setCreatedAt(message.getCreatedAt());
                vo.setOrderId(message.getOrderId());

                UserInfoVo user = userInfoMap.get(message.getSenderId());
                if (user != null) {
                    vo.setSenderUsername(user.getUsername());
                    vo.setSenderNickname(user.getNickname());
                    vo.setSenderAvatar(user.getAvatar());
                }

                return vo;
            })
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .collect(Collectors.toList());

        // 分页
        int page = queryDTO.getPage() != null ? queryDTO.getPage() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, messageList.size());

        List<MessageListVO> pagedList = messageList.subList(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pagedList);
        result.put("total", messageList.size());
        result.put("page", page);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public UserInfoVo getChatUserInfo(Long userId) {
        try {
            R feignResult = userFeignClient.getUserById(userId);
            if (feignResult.isSuccess()) {
                Object data = feignResult.get(R.DATA_TAG);
                if (data instanceof Map) {
                    return convertMapToUserInfoVo((Map<String, Object>) data);
                }
            }
        } catch (Exception e) {
            log.error("获取聊天用户信息失败，userId: {}, error: {}", userId, e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Object> getChatHistory(Long userId, Integer page, Integer pageSize) {
        Long currentUserId = getCurrentUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.and(inner -> inner.eq(Message::getSenderId, currentUserId).eq(Message::getReceiverId, userId))
                .or(inner -> inner.eq(Message::getSenderId, userId).eq(Message::getReceiverId, currentUserId)));
        wrapper.orderByDesc(Message::getCreatedAt);  // 降序，新的在前面

        Page<Message> pageParam = new Page<>(page, pageSize);
        IPage<Message> result = messageMapper.selectPage(pageParam, wrapper);


        List<ChatMessageVO> chatMessages = result.getRecords().stream()
            .map(message -> {
                ChatMessageVO vo = new ChatMessageVO();
                vo.setId(message.getId());
                vo.setContent(message.getContent());
                vo.setSenderId(message.getSenderId());
                vo.setReceiverId(message.getReceiverId());
                vo.setMessageType(message.getMessageType());
                vo.setFileUrl(message.getFileUrl());
                vo.setJumpUrl(message.getJumpUrl());
                vo.setCreatedAt(message.getCreatedAt());
                return vo;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("list", chatMessages);
        response.put("total", result.getTotal());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SendMessageResponseVO sendMessage(SendMessageDTO dto, MultipartFile[] images) {
        Long currentUserId = getCurrentUserId();

        Message message = new Message();
        message.setSenderId(currentUserId);
        message.setReceiverId(dto.getReceiverId());
        message.setMessageType(dto.getMessageType());
        message.setContent(dto.getContent());
        message.setOrderId(dto.getOrderId());
        message.setSenderType(dto.getSenderType());
        message.setIsRead(0);
        message.setCreatedAt(new Date());

        // 处理多图片上传
        if (images != null && images.length > 0) {
            try {
                List<String> uploadedUrls = new ArrayList<>();

                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        log.info("开始调用文件服务上传图片: {}", image.getOriginalFilename());

                        // 调用文件服务上传
                        R uploadResult = remoteFileService.upload(image);

                        if (uploadResult.isSuccess()) {
                            Object data = uploadResult.get(R.DATA_TAG);
                            if (data instanceof String) {
                                String fileUrl = (String) data;
                                uploadedUrls.add(fileUrl);
                                log.info("图片上传成功，URL: {}", fileUrl);
                            } else {
                                log.warn("文件服务返回的数据格式不正确: {}", data);
                                throw new RuntimeException("文件上传失败：返回数据格式错误");
                            }
                        } else {
                            log.error("文件服务上传失败: {}", uploadResult.get(R.MSG_TAG));
                            throw new RuntimeException("文件上传失败：" + uploadResult.get(R.MSG_TAG));
                        }
                    }
                }

                // 将多个图片URL用逗号连接存储
                if (!uploadedUrls.isEmpty()) {
                    String imageUrls = String.join(",", uploadedUrls);
                    message.setFileUrl(imageUrls);
                    log.info("多图片上传完成，URLs: {}", imageUrls);
                }

            } catch (Exception e) {
                log.error("调用文件服务失败", e);
                throw new RuntimeException("文件上传失败: " + e.getMessage());
            }
        }

        messageMapper.insert(message);

        // 通过 WebSocket 实时推送消息给接收者
        try {
            Long receiverId = message.getReceiverId();
            Long senderId = message.getSenderId();

            // 获取发送者信息用于显示
            String senderUsername = null;
            try {
                R senderResult = userFeignClient.getUserById(senderId);
                if (senderResult.isSuccess()) {
                    Object data = senderResult.get(R.DATA_TAG);
                    if (data instanceof Map) {
                        senderUsername = (String) ((Map<String, Object>) data).get("username");
                    }
                }
            } catch (Exception e) {
                log.warn("获取发送者信息失败: {}", e.getMessage());
            }

            Map<String, Object> wsData = new HashMap<>();
            wsData.put("id", message.getId());
            wsData.put("content", message.getContent());
            wsData.put("senderId", senderId);
            wsData.put("senderUsername", senderUsername != null ? senderUsername : "");
            wsData.put("senderType", message.getSenderType());
            wsData.put("receiverId", receiverId);
            wsData.put("messageType", message.getMessageType());
            wsData.put("fileUrl", message.getFileUrl());
            wsData.put("orderId", message.getOrderId());
            wsData.put("isRead", false);
            wsData.put("createdAt", message.getCreatedAt());
            wsData.put("timestamp", System.currentTimeMillis());

            webSocketFeignClient.pushNewMessage(receiverId, wsData);
            log.info("WebSocket 推送消息成功: receiverId={}, messageId={}", receiverId, message.getId());
        } catch (Exception e) {
            log.error("WebSocket 推送消息失败: {}", e.getMessage(), e);
        }

        SendMessageResponseVO response = new SendMessageResponseVO();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setSenderId(message.getSenderId());
        response.setReceiverId(message.getReceiverId());
        response.setMessageType(message.getMessageType());
        response.setFileUrl(message.getFileUrl());
        response.setCreatedAt(message.getCreatedAt());

        // 设置图片信息（如果有图片）
        if (images != null && images.length > 0 && images[0] != null && !images[0].isEmpty()) {
            response.setFileName(images[0].getOriginalFilename());
            response.setFileSize(images[0].getSize());
        }

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markMessageRead(Long messageId) {
        Message message = messageMapper.selectById(messageId);
        if (message != null) {
            message.setIsRead(1);
            messageMapper.updateById(message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> markAllMessagesRead() {
        Long currentUserId = getCurrentUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiverId, currentUserId)
               .eq(Message::getIsRead, 0);

        Message updateMessage = new Message();
        updateMessage.setIsRead(1);

        int updatedCount = messageMapper.update(updateMessage, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("updatedCount", updatedCount);
        return result;
    }

    @Override
    public UnreadCountVO getUnreadCount() {
        Long currentUserId = getCurrentUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiverId, currentUserId)
               .eq(Message::getIsRead, 0);

        List<Message> unreadMessages = messageMapper.selectList(wrapper);

        // 按发送者分组统计
        Map<Long, Long> unreadByUser = unreadMessages.stream()
            .collect(Collectors.groupingBy(Message::getSenderId, Collectors.counting()));

        UnreadCountVO result = new UnreadCountVO();
        result.setTotalUnread(unreadMessages.size());

        Map<String, Integer> unreadByUserStr = new HashMap<>();
        unreadByUser.forEach((userId, count) -> unreadByUserStr.put(userId.toString(), count.intValue()));
        result.setUnreadByUser(unreadByUserStr);

        return result;
    }

    @Override
    public Map<String, Object> getSystemHistory(Integer page, Integer pageSize) {
        Long currentUserId = getCurrentUserId();

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getSenderType, 2)
           .eq(Message::getReceiverId, currentUserId)
           .orderByDesc(Message::getCreatedAt);

        Page<Message> pageParam = new Page<>(page, pageSize);
        IPage<Message> result = messageMapper.selectPage(pageParam, wrapper);

        List<ChatMessageVO> chatMessages = result.getRecords().stream()
            .map(message -> {
                ChatMessageVO vo = new ChatMessageVO();
                vo.setId(message.getId());
                vo.setContent(message.getContent());
                vo.setSenderId(message.getSenderId());
                vo.setReceiverId(message.getReceiverId());
                vo.setMessageType(message.getMessageType());
                vo.setFileUrl(message.getFileUrl());
                vo.setJumpUrl(message.getJumpUrl());
                vo.setCreatedAt(message.getCreatedAt());
                return vo;
            })
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("list", chatMessages);
        response.put("total", result.getTotal());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return response;
    }

    @Override
    public List<ChatMessageVO> getOrderMessagesByOrderId(Long orderId) {
        List<Message> messages = messageMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Message>()
                .eq(Message::getOrderId, orderId)
                .orderByAsc(Message::getCreatedAt)
        );
        return messages.stream().map(this::toChatMessageVO).collect(java.util.stream.Collectors.toList());
    }

    private ChatMessageVO toChatMessageVO(Message message) {
        ChatMessageVO vo = new ChatMessageVO();
        vo.setId(message.getId());
        vo.setSenderId(message.getSenderId());
        vo.setReceiverId(message.getReceiverId());
        vo.setOrderId(message.getOrderId());
        vo.setContent(message.getContent());
        vo.setMessageType(message.getMessageType());
        vo.setFileUrl(message.getFileUrl());
        vo.setCreatedAt(message.getCreatedAt());
        return vo;
    }
} 