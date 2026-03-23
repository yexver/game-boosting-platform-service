package com.jmz.serverorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.SubmitOrderDTO;
import com.jmz.serverorder.entity.Orders;
import com.jmz.serverorder.mapper.OrdersMapper;
import com.jmz.serverorder.service.OrdersService;
import com.jmz.serverorder.vo.OrderSimpleDetailVO;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveruser.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import cn.hutool.core.util.IdUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.serverorder.entity.Message;
import com.jmz.serverorder.mapper.MessageMapper;
import com.jmz.serverorder.dto.OrdersQueryDTO;
import com.jmz.serverorder.dto.TakeOrderDTO;
import com.jmz.serverorder.vo.OrdersListVO;
import com.jmz.serverorder.vo.TakeOrderDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jmz.serveruser.vo.UserInfoVo;

import java.util.*;
import com.jmz.serveraccount.feign.UserAccountFeignClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.jmz.serveruser.entity.User;
import com.jmz.serverorder.mapper.UserMapper;
import com.jmz.serverorder.entity.OrderStatusLogs;
import com.jmz.serverorder.vo.OrderStatusLogVO;
import com.jmz.serverorder.mapper.OrderStatusLogsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;
import java.util.stream.Collectors;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.web.multipart.MultipartFile;
import com.jmz.serverorder.service.MessageServices;
import com.jmz.serverorder.dto.SendMessageDTO;
import com.jmz.serverorder.vo.SendMessageResponseVO;
import com.jmz.serverwebsocket.feign.WebSocketFeignClient;

import java.math.RoundingMode;
import com.jmz.serverorder.vo.GameOrderDistributionVO;
import com.jmz.serverorder.vo.IncomeTrendVO;
import com.jmz.serverorder.vo.OrderStatusPieVO;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserAccountFeignClient userAccountFeignClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OrderStatusLogsMapper orderStatusLogsMapper;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private MessageServices messageServices;

    @Autowired
    private WebSocketFeignClient webSocketFeignClient;

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

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUser) {
            return ((LoginUser) principal).getUserId();
        }
        return null;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Object submitOrder(SubmitOrderDTO dto) {
        // 获取当前登录用户ID
        Long publisherId = getCurrentUserId();
        if (publisherId == null) {
            throw new RuntimeException("无法获取当前登录用户ID");
        }

        // 密码验证
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("请输入密码");
        }


        // 查询用户信息
        User user = userMapper.selectById(publisherId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        Orders order = new Orders();
        order.setGameId(dto.getGameId());
        order.setSystemId(dto.getSystemId());
        order.setServerId(dto.getServerId());
        order.setBoostingType(dto.getBoostingType());
        order.setTitle(dto.getTitle());
        order.setDescription(dto.getDescription());
        order.setAccountInfo(dto.getAccountInfo());
        order.setPrice(dto.getPrice());
        order.setSecurityDeposit(dto.getSecurityDeposit());
        order.setEfficiencyDeposit(dto.getEfficiencyDeposit());
        order.setTimeLimit(dto.getTimeLimit());
        order.setOrderNo("ORD" + IdUtil.getSnowflake().nextIdStr());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setPublisherId(publisherId);
        // 统一计费逻辑：平台服务费=订单金额*5%，封顶25元
        BigDecimal platformFee = dto.getPrice().multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        if (platformFee.compareTo(new BigDecimal("25")) > 0) {
            platformFee = new BigDecimal("25.00");
        }
        order.setPlatformFee(platformFee);

        // 冻结用户资金（扣除余额，增加冻结金额）
        AccountAdjustDTO adjustDTO = new AccountAdjustDTO();
        adjustDTO.setUserId(publisherId);
        adjustDTO.setAmount(dto.getPrice()); // 假设冻结全部订单金额
        adjustDTO.setType(7); // 7=冻结，具体类型可根据业务调整
        adjustDTO.setRemark("订单提交冻结资金，订单号:" + order.getOrderNo());
        R freezeResult = userAccountFeignClient.adjustAccountBalance(adjustDTO);
        if (freezeResult == null || !freezeResult.isSuccess()) {
            throw new RuntimeException(String.valueOf(freezeResult.get("msg")));
        }

        ordersMapper.insert(order);

        Message message = new Message();
        message.setOrderId(order.getId());
        message.setSenderId(0L); // 系统
        message.setReceiverId(publisherId);
        message.setSenderType(2); // 系统
        message.setMessageType(1);
        message.setContent("您的订单已创建成功！");
        message.setIsRead(0);
        message.setCreatedAt(new Date());
        messageMapper.insert(message);

        // 通过 WebSocket 实时推送消息给发单人
        try {
            Map<String, Object> wsData = new HashMap<>();
            wsData.put("id", message.getId());
            wsData.put("content", message.getContent());
            wsData.put("senderId", 0L);
            wsData.put("senderUsername", "系统");
            wsData.put("senderType", 2);
            wsData.put("receiverId", publisherId);
            wsData.put("messageType", 1);
            wsData.put("orderId", order.getId());
            wsData.put("isRead", false);
            wsData.put("createdAt", message.getCreatedAt());
            wsData.put("timestamp", System.currentTimeMillis());
            webSocketFeignClient.pushNewMessage(publisherId, wsData);
            log.info("订单创建 WebSocket 推送成功: publisherId={}, orderId={}", publisherId, order.getId());
        } catch (Exception e) {
            log.error("订单创建 WebSocket 推送失败: publisherId={}, error={}", publisherId, e.getMessage());
        }

        return order.getId();
    }


    @Override
    public Map<String, Object> orderInfoList(OrdersQueryDTO queryDTO) {
        int page = queryDTO.getPage() != null ? queryDTO.getPage() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
        int offset = (page - 1) * pageSize;

        List<OrdersListVO> list = ordersMapper.selectOrderInfoList(queryDTO, offset, pageSize);
        int total = ordersMapper.countOrderInfoList(queryDTO);

        // 统计并赋值近30天客服介入率
        for (OrdersListVO vo : list) {
            Long publisherId = vo.getPublisherId();
            if (publisherId != null) {
                int totalOrders = ordersMapper.countOrdersByPublisherIn30Days(publisherId);
                int managerOrders = ordersMapper.countManagerOrdersByPublisherIn30Days(publisherId);
                Double rate = (totalOrders == 0) ? null : (managerOrders * 1.0 / totalOrders);
                vo.setPublisherManagerRate30d(rate);
                vo.setPublisherOrderCount30d(totalOrders); // 新增赋值
            } else {
                vo.setPublisherManagerRate30d(null);
                vo.setPublisherOrderCount30d(null);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    // 订单完成时调用
    public void completeOrder(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) return;
        Long publisherId = order.getPublisherId();
        AccountAdjustDTO adjustDTO = new AccountAdjustDTO();
        adjustDTO.setUserId(publisherId);
        adjustDTO.setAmount(order.getPrice());
        adjustDTO.setType(8); // 解冻/扣除冻结金额
        adjustDTO.setRemark("订单完成解冻/扣除冻结资金，订单号:" + order.getOrderNo());
        R unfreezeResult = userAccountFeignClient.unfreezeAccount(adjustDTO);
        if (unfreezeResult == null || !unfreezeResult.isSuccess()) {
            throw new RuntimeException("解冻/扣除冻结资金失败");
        }
    }
    
    @Override
    public TakeOrderDetailVO getTakeOrderDetail(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        
        TakeOrderDetailVO orderDetail = ordersMapper.selectTakeOrderDetail(orderId);
        if (orderDetail == null) {
            throw new RuntimeException("订单不存在");
        }
        
        return orderDetail;
    }
    
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Object takeOrder(TakeOrderDTO dto) {
        // 获取当前登录用户ID
        Long takerId = getCurrentUserId();
        if (takerId == null) {
            throw new RuntimeException("无法获取当前登录用户ID");
        }

        // 密码验证
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new RuntimeException("请输入密码");
        }

        // 查询用户信息
        User user = userMapper.selectById(takerId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 查询订单信息
        Orders order = ordersMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态（1-未接手状态才允许接单）
        if (order.getStatus() == null || order.getStatus() != 1) {
            throw new RuntimeException("订单状态不允许接单，只有未接手的订单才能接单");
        }
        
        // 检查是否是自己发布的订单
        if (order.getPublisherId().equals(takerId)) {
            throw new RuntimeException("不能接自己发布的订单");
        }

        // 冻结接单者资金（扣除余额，增加冻结金额）
        AccountAdjustDTO takerAdjustDTO = new AccountAdjustDTO();
        takerAdjustDTO.setUserId(takerId);
        takerAdjustDTO.setAmount(order.getPrice()); // 冻结订单金额
        takerAdjustDTO.setType(7); // 7=冻结，具体类型可根据业务调整
        takerAdjustDTO.setRemark("接单冻结资金，订单号:" + order.getOrderNo());
        R takerFreezeResult = userAccountFeignClient.adjustAccountBalance(takerAdjustDTO);
        if (takerFreezeResult == null || !takerFreezeResult.isSuccess()) {
            throw new RuntimeException("接单失败: " + takerFreezeResult.get("msg"));
        }

        // 更新订单状态为代练中（2-代练中）
        order.setStatus(2);
        order.setTakerId(takerId);
        order.setStartAt(new Date());
        order.setUpdatedAt(new Date());
        ordersMapper.updateById(order);

        // 添加订单状态日志
        OrderStatusLogs statusLog = new OrderStatusLogs();
        statusLog.setOrderId(order.getId());
        statusLog.setFromStatus(1); // 原状态：未接手
        statusLog.setToStatus(2);   // 新状态：代练中
        statusLog.setOperatorId(takerId); // 操作者：接单人
        statusLog.setOperatorType(2); // 操作者类型：2-接手者
        statusLog.setRemark("接单人" + user.getUsername() + "成功接单");
        statusLog.setCreatedAt(new Date());
        orderStatusLogsMapper.insert(statusLog);

        // 发送系统消息给发布者
        Message message = new Message();
        message.setOrderId(order.getId());
        message.setSenderId(0L); // 系统
        message.setReceiverId(order.getPublisherId());
        message.setSenderType(2); // 系统
        message.setMessageType(1);
        message.setContent("您的订单" + order.getOrderNo() + "已被接单！");
        message.setIsRead(0);
        message.setCreatedAt(new Date());
        messageMapper.insert(message);

        // 发送系统消息给接单者
        Message takerMessage = new Message();
        takerMessage.setOrderId(order.getId());
        takerMessage.setSenderId(0L); // 系统
        takerMessage.setReceiverId(takerId);
        takerMessage.setSenderType(2); // 系统
        takerMessage.setMessageType(1);
        takerMessage.setContent("您已成功接单：" + order.getOrderNo());
        takerMessage.setIsRead(0);
        takerMessage.setCreatedAt(new Date());
        messageMapper.insert(takerMessage);

        return "接单成功";
    }

    @Override
    public OrderSimpleDetailVO getOrderSimpleDetail(Long orderId) {
        // 1. 获取订单详情
        TakeOrderDetailVO orderDetail = getTakeOrderDetail(orderId);
        if (orderDetail == null) {
            throw new RuntimeException("订单不存在");
        }

        // 2. 获取发单人信息
        User publisherUser = userMapper.selectById(orderDetail.getPublisherId());

        // 3. 获取接单人ID（单独查订单表）
        Orders order = ordersMapper.selectById(orderId);
        Long takerId = order.getTakerId();
        User takerUser = takerId != null ? userMapper.selectById(takerId) : null;

        // 4. 组装VO
        OrderSimpleDetailVO vo = new OrderSimpleDetailVO();
        vo.setId(orderDetail.getId());
        vo.setOrderNo(orderDetail.getOrderNo());
        vo.setTitle(orderDetail.getTitle());
        vo.setGameId(orderDetail.getGameId());
        vo.setSystemId(orderDetail.getSystemId());
        vo.setServerId(orderDetail.getServerId());
        vo.setBoostingType(orderDetail.getBoostingType());
        vo.setDescription(orderDetail.getDescription());
        vo.setAccountInfo(orderDetail.getAccountInfo());
        vo.setTimeLimit(orderDetail.getTimeLimit());
        vo.setPrice(orderDetail.getPrice());
        vo.setSecurityDeposit(orderDetail.getSecurityDeposit());
        vo.setEfficiencyDeposit(orderDetail.getEfficiencyDeposit());
        vo.setStatus(orderDetail.getStatus());
        vo.setCreatedAt(orderDetail.getCreatedAt());
        vo.setStartAt(orderDetail.getStartAt());
        vo.setActualAt(orderDetail.getActualAt());
        vo.setGameIcon(orderDetail.getGameIcon());
        vo.setSystemIcon(orderDetail.getSystemIcon());
        vo.setGameName(orderDetail.getGameName());
        vo.setSystemName(orderDetail.getSystemName());
        vo.setServerName(orderDetail.getServerName());

        // 5. 设置发单人信息
        if (publisherUser != null) {
            OrderSimpleDetailVO.SimpleUserVO publisher = new OrderSimpleDetailVO.SimpleUserVO();
            publisher.setUserId(publisherUser.getUserId());
            publisher.setUsername(publisherUser.getUsername());
            publisher.setAvatar(publisherUser.getAvatar());
            vo.setPublisher(publisher);
        }

        // 6. 设置接单人信息
        if (takerUser != null) {
            OrderSimpleDetailVO.SimpleUserVO taker = new OrderSimpleDetailVO.SimpleUserVO();
            taker.setUserId(takerUser.getUserId());
            taker.setUsername(takerUser.getUsername());
            taker.setAvatar(takerUser.getAvatar());
            vo.setTaker(taker);
        }

        // 7. 查询订单状态日志
        LambdaQueryWrapper<OrderStatusLogs> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(OrderStatusLogs::getOrderId, orderId)
                 .orderByDesc(OrderStatusLogs::getCreatedAt);
        List<OrderStatusLogs> statusLogs = orderStatusLogsMapper.selectList(logWrapper);
        
        List<OrderStatusLogVO> statusLogVOs = statusLogs.stream().map(log -> {
            OrderStatusLogVO logVO = new OrderStatusLogVO();
            logVO.setId(log.getId());
            logVO.setOrderId(log.getOrderId());
            logVO.setFromStatus(log.getFromStatus());
            logVO.setToStatus(log.getToStatus());
            logVO.setOperatorId(log.getOperatorId());
            logVO.setOperatorType(log.getOperatorType());
            logVO.setRemark(log.getRemark());
            logVO.setImageUrls(log.getImageUrls());
            logVO.setCreatedAt(log.getCreatedAt());
            logVO.setPrice(log.getPrice());
            logVO.setDeposit(log.getDeposit());
            
            // 设置操作者信息
            if (log.getOperatorId() != null) {
                User operatorUser = userMapper.selectById(log.getOperatorId());
                if (operatorUser != null) {
                    logVO.setOperatorName(operatorUser.getUsername());
                    logVO.setOperatorAvatar(operatorUser.getAvatar());
                }
            }
            
            return logVO;
        }).collect(Collectors.toList());
        
        vo.setStatusLogs(statusLogVOs);

        return vo;
    }

    @Override
    public R applyRevoke(Long orderId, BigDecimal price, BigDecimal deposit, String remark, MultipartFile[] images) throws Exception {
        // 1. 上传图片，获取图片路径（调用文件服务）
        String imageUrls = null;
        if (images != null && images.length > 0) {
            R uploadResult = remoteFileService.multiUpload(images);
            if (uploadResult.isSuccess() && uploadResult.get(R.DATA_TAG) != null) {
                Object data = uploadResult.get(R.DATA_TAG);
                if (data instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> urlList = (java.util.List<String>) data;
                    imageUrls = String.join(",", urlList);
                } else if (data instanceof String) {
                    imageUrls = data.toString();
                }
            } else {
                throw new RuntimeException("文件上传失败: " + uploadResult.get(R.MSG_TAG));
            }
        }
        // 2. 插入订单状态日志，状态变更为7-撤销中
        Orders order = ordersMapper.selectById(orderId);
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        if (order != null) {
            log.setFromStatus(order.getStatus()); // 撤销前的订单状态
        }
        log.setToStatus(7); // 7-撤销中
        Long currentUserId = getCurrentUserId();
        if (order != null) {
            if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
                log.setOperatorType(1); // 发单者
            } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
                log.setOperatorType(2); // 接单者
            }
        }
        log.setOperatorId(currentUserId);
        log.setRemark(remark);
        log.setImageUrls(imageUrls);
        log.setCreatedAt(new java.util.Date());
        log.setPrice(price);
        log.setDeposit(deposit);
        orderStatusLogsMapper.insert(log);
        // 3. 更新订单状态为7-撤销中
        if (order != null) {
            order.setStatus(7);
            order.setUpdatedAt(new java.util.Date());
            ordersMapper.updateById(order);
        }
        // 4. 申请撤销后，给对方用户发消息
        if (order != null) {
            Long receiverId = null;
            if (currentUserId != null) {
                if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                    receiverId = order.getTakerId();
                } else if (currentUserId.equals(order.getTakerId())) {
                    receiverId = order.getPublisherId();
                }
            }
            if (receiverId != null) {
                SendMessageDTO msgDto = new SendMessageDTO();
                msgDto.setReceiverId(receiverId);
                msgDto.setOrderId(orderId);
                msgDto.setContent("您的订单" + order.getOrderNo() + "已发起撤销申请");
                msgDto.setMessageType(2); // 2为图片消息类型
                msgDto.setSenderType(2); // 可根据业务补充
                msgDto.setImageUrls(imageUrls); // 直接传已上传的图片URL
                messageServices.sendMessage(msgDto, null); // 不再传images
            }
        }
        return R.success("撤销申请已提交");
    }

    @Override
    public R cancelRevoke(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        // 记录撤销前的状态，通常撤销中为7，恢复为2-代练中（如有更复杂业务可调整）
        int fromStatus = order.getStatus();
        int toStatus = 2; // 默认恢复为2-代练中
        order.setStatus(toStatus);
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);

        // 插入状态日志
        Long currentUserId = getCurrentUserId();
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setOperatorId(currentUserId);
        if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
            log.setOperatorType(1); // 发单者
        } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
            log.setOperatorType(2); // 接单者
        }
        log.setRemark("撤销申请已取消");
        log.setCreatedAt(new java.util.Date());
        orderStatusLogsMapper.insert(log);

        // 给对方用户发消息
        Long receiverId = null;
        if (currentUserId != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() +"撤销申请已被取消");
            msgDto.setMessageType(1); // 普通文本消息
            msgDto.setSenderType(null);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("撤销申请已取消");
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public R applyAccept(Long orderId, String remark, MultipartFile[] images) throws Exception {
        // 1. 上传图片，获取图片路径（调用文件服务）
        String imageUrls = null;
        if (images != null && images.length > 0) {
            R uploadResult = remoteFileService.multiUpload(images);
            if (uploadResult.isSuccess() && uploadResult.get(R.DATA_TAG) != null) {
                Object data = uploadResult.get(R.DATA_TAG);
                if (data instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> urlList = (java.util.List<String>) data;
                    imageUrls = String.join(",", urlList);
                } else if (data instanceof String) {
                    imageUrls = data.toString();
                }
            } else {
                throw new RuntimeException("文件上传失败: " + uploadResult.get(R.MSG_TAG));
            }
        }
        // 2. 插入订单状态日志，状态变更为3-待验收
        Orders order = ordersMapper.selectById(orderId);
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        if (order != null) {
            log.setFromStatus(order.getStatus());
        }
        log.setToStatus(3); // 3-待验收
        Long currentUserId = getCurrentUserId();
        if (order != null) {
            if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
                log.setOperatorType(1); // 发单者
            } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
                log.setOperatorType(2); // 接单者
            }
        }
        log.setOperatorId(currentUserId);
        log.setRemark(remark);
        log.setImageUrls(imageUrls);
        log.setCreatedAt(new java.util.Date());
        // 不再set price 和 deposit 字段
        orderStatusLogsMapper.insert(log);
        // 3. 更新订单状态为3-待验收
        if (order != null) {
            order.setStatus(3);
            order.setUpdatedAt(new java.util.Date());
            order.setActualAt(new java.util.Date());
            ordersMapper.updateById(order);
        }
        // 4. 给对方用户发消息
        Long receiverId = null;
        if (currentUserId != null && order != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() +"已完成请验收");
            msgDto.setMessageType(2); // 2为图片消息类型
            msgDto.setSenderType(null);
            msgDto.setImageUrls(imageUrls);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("验收申请已提交");
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public R verifyAccept(Long orderId, BigDecimal deposit, String remark, MultipartFile[] images) throws Exception {
        // 1. 上传图片，获取图片路径（调用文件服务）
        String imageUrls = null;
        if (images != null && images.length > 0) {
            R uploadResult = remoteFileService.multiUpload(images);
            if (uploadResult.isSuccess() && uploadResult.get(R.DATA_TAG) != null) {
                Object data = uploadResult.get(R.DATA_TAG);
                if (data instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> urlList = (java.util.List<String>) data;
                    imageUrls = String.join(",", urlList);
                } else if (data instanceof String) {
                    imageUrls = data.toString();
                }
            } else {
                throw new RuntimeException("文件上传失败: " + uploadResult.get(R.MSG_TAG));
            }
        }
        // 2. 插入订单状态日志，状态变更为5-已完成
        Orders order = ordersMapper.selectById(orderId);

        // 先声明并赋值
        Long currentUserId = getCurrentUserId();

        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        if (order != null) {
            if (currentUserId == null || !currentUserId.equals(order.getPublisherId())) {
                throw new RuntimeException("只有发单者才能进行验收");
            }
            log.setFromStatus(order.getStatus());
        }
        log.setToStatus(5); // 5-已完成
        if (order != null) {
            if (currentUserId.equals(order.getPublisherId())) {
                log.setOperatorType(1); // 发单者
            } else if (currentUserId.equals(order.getTakerId())) {
                log.setOperatorType(2); // 接单者
            }
        }
        log.setOperatorId(currentUserId);
        log.setRemark(remark);
        log.setImageUrls(imageUrls);
        log.setCreatedAt(new java.util.Date());
        log.setPrice(order.getPrice());
        log.setDeposit(deposit);
        // 统一计费逻辑：平台服务费=订单金额*5%，封顶25元
        BigDecimal orderPrice = order.getPrice();
        BigDecimal platformFee = orderPrice.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        if (platformFee.compareTo(new BigDecimal("25")) > 0) {
            platformFee = new BigDecimal("25.00");
        }
        order.setPlatformFee(platformFee);
        // 更新订单的 platformFee 字段
        ordersMapper.updateById(order);
        BigDecimal takerIncome = orderPrice.subtract(platformFee);
        // 0. 解冻并扣除发单人的订单金额（price）
        AccountAdjustDTO publisherUnfreezePrice = new AccountAdjustDTO();
        publisherUnfreezePrice.setUserId(order.getPublisherId());
        publisherUnfreezePrice.setAmount(orderPrice); // 订单金额
        publisherUnfreezePrice.setType(8); // 8=解冻并扣除冻结金额
        publisherUnfreezePrice.setRemark("订单验收结算，订单号:" + order.getOrderNo());
        publisherUnfreezePrice.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(publisherUnfreezePrice);
        // 1. 计算代练实际冻结的保证金
        BigDecimal totalDeposit = order.getSecurityDeposit().add(
            order.getEfficiencyDeposit() != null ? order.getEfficiencyDeposit() : BigDecimal.ZERO
        );
        System.out.println("totalDeposit:" + totalDeposit);
        // 2. 解冻并扣除全部保证金
        AccountAdjustDTO takerDepositUnfreeze = new AccountAdjustDTO();
        takerDepositUnfreeze.setUserId(order.getTakerId());
        takerDepositUnfreeze.setAmount(totalDeposit);
        takerDepositUnfreeze.setType(8); // 8=解冻并扣除冻结金额
        takerDepositUnfreeze.setRemark("订单验收保证金结算，订单号:" + order.getOrderNo());
        takerDepositUnfreeze.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(takerDepositUnfreeze);

        // 3. 赔付给发单者
        if (deposit.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO publisherAddDeposit = new AccountAdjustDTO();
            publisherAddDeposit.setUserId(order.getPublisherId());
            publisherAddDeposit.setAmount(deposit);
            publisherAddDeposit.setType(1); // 1=增加余额
            publisherAddDeposit.setRemark("订单验收保证金赔付，订单号:" + order.getOrderNo());
            publisherAddDeposit.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(publisherAddDeposit);
        }

        // 4. 剩余保证金返还给代练
        BigDecimal takerReturnDeposit = totalDeposit.subtract(deposit);
        if (takerReturnDeposit.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO takerReturnDepositDto = new AccountAdjustDTO();
            takerReturnDepositDto.setUserId(order.getTakerId());
            takerReturnDepositDto.setAmount(takerReturnDeposit);
            takerReturnDepositDto.setType(1); // 1=增加余额
            takerReturnDepositDto.setRemark("订单验收返还保证金，订单号:" + order.getOrderNo());
            takerReturnDepositDto.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(takerReturnDepositDto);
        }

        // 5.给接单人（代练）发放收益
        AccountAdjustDTO takerAdd = new AccountAdjustDTO();
        takerAdd.setUserId(order.getTakerId());
        takerAdd.setAmount(takerIncome); // takerIncome = orderPrice.subtract(platformFee)
        takerAdd.setType(1); // 1=增加余额
        takerAdd.setRemark("订单验收结算，订单号:" + order.getOrderNo() + "，已扣除平台服务费" + platformFee + "元");
        takerAdd.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(takerAdd);

        orderStatusLogsMapper.insert(log);
        // 3. 更新订单状态为5-已完成
        if (order != null) {
            order.setStatus(5);
            order.setActualAt(new java.util.Date()); // 完成时间
            order.setUpdatedAt(new java.util.Date());
            ordersMapper.updateById(order);
        }
        // 4. 给对方用户发消息
        Long receiverId = null;
        if (currentUserId != null && order != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + (order != null ? order.getOrderNo() : orderId) + "已验收完成");
            msgDto.setMessageType(2); // 2为图片消息类型
            msgDto.setSenderType(null);
            msgDto.setImageUrls(imageUrls);
            messageServices.sendMessage(msgDto, null);
        }
       //系统还要发送一个消息给接单者，订单已验收完成
        if (order != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(order.getTakerId());
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已验收完成");
            msgDto.setMessageType(2);
            msgDto.setSenderType(null);
            msgDto.setImageUrls(imageUrls);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("订单已验收完成");
    }
    // 发单者取消未接手的订单
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public R cancelOrder(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        // 只能未接手状态（1）且当前用户为发单者才能撤销
        Long currentUserId = getCurrentUserId();
        // 更新订单状态为6-已撤销
        int fromStatus = order.getStatus();
        order.setStatus(6); // 6-已撤销
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);
        // 插入状态日志
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(6);
        log.setOperatorId(currentUserId);
        log.setOperatorType(1); // 发单者
        log.setRemark("发单者主动撤销订单");
        log.setCreatedAt(new java.util.Date());
        // 解冻发单者资金（因为是未接手状态取消，全额返还）
        AccountAdjustDTO unfreezeDTO = new AccountAdjustDTO();
        unfreezeDTO.setUserId(order.getPublisherId());
        unfreezeDTO.setAmount(order.getPrice());
        unfreezeDTO.setType(8); // 解冻并扣除
        unfreezeDTO.setRemark("订单取消解冻资金，订单号:" + order.getOrderNo());
        unfreezeDTO.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(unfreezeDTO);

        // 返还资金给发单者
        AccountAdjustDTO returnDTO = new AccountAdjustDTO();
        returnDTO.setUserId(order.getPublisherId());
        returnDTO.setAmount(order.getPrice());
        returnDTO.setType(1); // 增加余额
        returnDTO.setRemark("订单取消返还资金，订单号:" + order.getOrderNo());
        returnDTO.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(returnDTO);
        orderStatusLogsMapper.insert(log);
        return R.success("订单已撤销");
    }


    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public R agreeRevoke(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        int fromStatus = order.getStatus();
        // 只有撤销中(7)状态才能同意撤销
        if (fromStatus != 7) {
            return R.error("只有撤销中的订单才能同意撤销");
        }
        order.setStatus(6); // 6-已撤销
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);
        // 插入状态日志
        Long currentUserId = getCurrentUserId();
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(6);
        log.setOperatorId(currentUserId);
        if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
            log.setOperatorType(1);
        } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
            log.setOperatorType(2);
        }
        log.setRemark("同意撤销");
        log.setCreatedAt(new java.util.Date());
        // 查询上一条日志金额
        OrderStatusLogs lastLog = orderStatusLogsMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderStatusLogs>()
                .eq(OrderStatusLogs::getOrderId, orderId)
                .orderByDesc(OrderStatusLogs::getCreatedAt)
                .last("LIMIT 1")
        );
        if (lastLog != null) {
            log.setPrice(lastLog.getPrice());
            log.setDeposit(lastLog.getDeposit());
        }
        orderStatusLogsMapper.insert(log);
        // 查询申请撤销日志（fromStatus=2, toStatus=7）
        OrderStatusLogs applyLog = orderStatusLogsMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderStatusLogs>()
                .eq(OrderStatusLogs::getOrderId, orderId)
                .eq(OrderStatusLogs::getFromStatus, 2)
                .eq(OrderStatusLogs::getToStatus, 7)
                .orderByDesc(OrderStatusLogs::getCreatedAt)
                .last("LIMIT 1")
        );
        BigDecimal orderPrice = order.getPrice();
        BigDecimal orderDeposit = order.getSecurityDeposit().add(order.getEfficiencyDeposit());//订单总保证金
        BigDecimal applyPrice = applyLog != null && applyLog.getPrice() != null ? applyLog.getPrice() : BigDecimal.ZERO;
        BigDecimal applyDeposit = applyLog != null && applyLog.getDeposit() != null ? applyLog.getDeposit() : BigDecimal.ZERO;
        // 1. 解冻并扣除发单人全部订单金额
        AccountAdjustDTO publisherUnfreeze = new AccountAdjustDTO();
        publisherUnfreeze.setUserId(order.getPublisherId());
        publisherUnfreeze.setAmount(orderPrice);
        publisherUnfreeze.setType(8);
        publisherUnfreeze.setRemark("订单撤销结算，订单号:" + order.getOrderNo());
        publisherUnfreeze.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(publisherUnfreeze);
        // 2. 返还剩余金额给发单人
        BigDecimal publisherReturn = orderPrice.subtract(applyPrice);
        if (publisherReturn.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO publisherReturnDto = new AccountAdjustDTO();
            publisherReturnDto.setUserId(order.getPublisherId());
            publisherReturnDto.setAmount(publisherReturn);
            publisherReturnDto.setType(1);
            publisherReturnDto.setRemark("订单撤销返还，订单号:" + order.getOrderNo());
            publisherReturnDto.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(publisherReturnDto);
        }
        // 3. 发给代练申请撤销金额
        if (applyPrice.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO takerAdd = new AccountAdjustDTO();
            takerAdd.setUserId(order.getTakerId());
            takerAdd.setAmount(applyPrice);
            takerAdd.setType(1);
            takerAdd.setRemark("订单撤销结算，订单号:" + order.getOrderNo());
            takerAdd.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(takerAdd);
        }
        // 4. 解冻并扣除代练全部保证金
        AccountAdjustDTO takerDepositUnfreeze = new AccountAdjustDTO();
        takerDepositUnfreeze.setUserId(order.getTakerId());
        takerDepositUnfreeze.setAmount(orderDeposit);
        takerDepositUnfreeze.setType(8);
        takerDepositUnfreeze.setRemark("订单撤销保证金结算，订单号:" + order.getOrderNo());
        takerDepositUnfreeze.setOrderId(order.getId());
        userAccountFeignClient.adjustAccountBalance(takerDepositUnfreeze);
        // 5. 申请撤销保证金发给发单人
        if (applyDeposit.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO publisherAddDeposit = new AccountAdjustDTO();
            publisherAddDeposit.setUserId(order.getPublisherId());
            publisherAddDeposit.setAmount(applyDeposit);
            publisherAddDeposit.setType(1);
            publisherAddDeposit.setRemark("订单撤销保证金收入，订单号:" + order.getOrderNo());
            publisherAddDeposit.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(publisherAddDeposit);
        }
        // 6. 剩余保证金返还给代练
        BigDecimal takerReturnDeposit = orderDeposit.subtract(applyDeposit);
        if (takerReturnDeposit.compareTo(BigDecimal.ZERO) > 0) {
            AccountAdjustDTO takerReturnDepositDto = new AccountAdjustDTO();
            takerReturnDepositDto.setUserId(order.getTakerId());
            takerReturnDepositDto.setAmount(takerReturnDeposit);
            takerReturnDepositDto.setType(1);
            takerReturnDepositDto.setRemark("订单撤销返还保证金，订单号:" + order.getOrderNo());
            takerReturnDepositDto.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(takerReturnDepositDto);
        }
        // 通知对方
        Long receiverId = null;
        if (currentUserId != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "撤销申请已同意，订单已撤销");
            msgDto.setMessageType(1);
            msgDto.setSenderType(null);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("已同意撤销，订单已撤销");
    }

    @Override
    public R disagreeRevoke(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        int fromStatus = order.getStatus();
        // 只有撤销中(7)状态才能拒绝撤销
        if (fromStatus != 7) {
            return R.error("只有撤销中的订单才能拒绝撤销");
        }
        order.setStatus(2); // 2-代练中
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);
        // 插入状态日志
        Long currentUserId = getCurrentUserId();
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(2);
        log.setOperatorId(currentUserId);
        if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
            log.setOperatorType(1);
        } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
            log.setOperatorType(2);
        }
        log.setRemark("拒绝撤销");
        log.setCreatedAt(new java.util.Date());
        orderStatusLogsMapper.insert(log);
        // 通知对方
        Long receiverId = null;
        if (currentUserId != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "撤销申请已被拒绝，订单继续进行");
            msgDto.setMessageType(1);
            msgDto.setSenderType(null);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("已拒绝撤销，订单继续进行");
    }

    @Override
    public R interveneOrder(Long orderId) {
        Long currentUserId = getCurrentUserId();
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        int fromStatus = order.getStatus();
        // 只有待介入(8)状态才能正式介入
        if (fromStatus != 8) {
            return R.error("只有待介入的订单才能进行平台介入");
        }
        order.setManagerId(currentUserId);
        order.setStatus(9); // 9-介入中
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);
        // 插入状态日志

        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(9);
        log.setOperatorId(currentUserId);
        log.setOperatorType(3); // 3-平台客服/管理员
        log.setRemark("平台已正式介入订单");
        log.setCreatedAt(new java.util.Date());
        orderStatusLogsMapper.insert(log);
        // 通知发单者和接单者
        if (order.getPublisherId() != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(order.getPublisherId());
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已被平台正式介入");
            msgDto.setMessageType(1);
            msgDto.setSenderType(3); // 平台
            messageServices.sendMessage(msgDto, null);
        }
        if (order.getTakerId() != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(order.getTakerId());
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已被平台正式介入");
            msgDto.setMessageType(1);
            msgDto.setSenderType(3); // 平台
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("平台已正式介入订单");
    }

    @Override
    public R applyIntervene(Long orderId, BigDecimal price, BigDecimal deposit, String remark, MultipartFile[] images) throws Exception {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        int fromStatus = order.getStatus();

        // 上传图片
        String imageUrls = null;
        if (images != null && images.length > 0) {
            R uploadResult = remoteFileService.multiUpload(images);
            if (uploadResult.isSuccess() && uploadResult.get(R.DATA_TAG) != null) {
                Object data = uploadResult.get(R.DATA_TAG);
                if (data instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> urlList = (java.util.List<String>) data;
                    imageUrls = String.join(",", urlList);
                } else if (data instanceof String) {
                    imageUrls = data.toString();
                }
            } else {
                throw new RuntimeException("文件上传失败: " + uploadResult.get(R.MSG_TAG));
            }
        }
        order.setStatus(8); // 8-待介入
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);

        // 插入状态日志
        Long currentUserId = getCurrentUserId();
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(8);
        log.setOperatorId(currentUserId);
        if (currentUserId != null && currentUserId.equals(order.getPublisherId())) {
            log.setOperatorType(1);
        } else if (currentUserId != null && currentUserId.equals(order.getTakerId())) {
            log.setOperatorType(2);
        }
        log.setRemark(remark != null ? remark : "用户申请平台介入");
        log.setCreatedAt(new java.util.Date());
        log.setPrice(price);
        log.setDeposit(deposit);
        log.setImageUrls(imageUrls);
        orderStatusLogsMapper.insert(log);

        // 通知对方
        Long receiverId = null;
        if (currentUserId != null) {
            if (currentUserId.equals(order.getPublisherId()) && order.getTakerId() != null) {
                receiverId = order.getTakerId();
            } else if (currentUserId.equals(order.getTakerId())) {
                receiverId = order.getPublisherId();
            }
        }
        if (receiverId != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(receiverId);
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已申请平台介入");
            msgDto.setMessageType(2);
            msgDto.setSenderType(null);
            msgDto.setImageUrls(imageUrls);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("已申请平台介入");
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public R arbitrateOrder(Long orderId, BigDecimal payAmount, BigDecimal depositAmount, String remark) throws Exception {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        int fromStatus = order.getStatus();
        // 只有介入中(9)状态才能仲裁
        if (fromStatus != 9) {
            return R.error("只有介入中的订单才能仲裁");
        }
        order.setStatus(10); // 10-已仲裁
        order.setUpdatedAt(new java.util.Date());
        ordersMapper.updateById(order);
        // 插入状态日志
        Long currentUserId = getCurrentUserId();
        OrderStatusLogs log = new OrderStatusLogs();
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(10);
        log.setOperatorId(currentUserId);
        log.setOperatorType(3); // 3-平台客服/管理员
        log.setRemark(remark != null ? remark : "平台仲裁处理");
        log.setCreatedAt(new java.util.Date());
        log.setPrice(payAmount);
        log.setDeposit(depositAmount);
        orderStatusLogsMapper.insert(log);
        // 资金结算
        if (order.getPublisherId() != null && order.getTakerId() != null) {
            // 解冻并扣除发单人全部冻结金额（orderPrice），再按仲裁结果分配：
            AccountAdjustDTO publisherUnfreeze = new AccountAdjustDTO();
            publisherUnfreeze.setUserId(order.getPublisherId());
            publisherUnfreeze.setAmount(order.getPrice()); // amount = orderPrice
            publisherUnfreeze.setType(8); // 8=解冻并扣除冻结金额
            publisherUnfreeze.setRemark("订单仲裁结算，订单号:" + order.getOrderNo());
            publisherUnfreeze.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(publisherUnfreeze);

            if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
                AccountAdjustDTO takerAdd = new AccountAdjustDTO();
                takerAdd.setUserId(order.getTakerId());
                takerAdd.setAmount(payAmount);
                takerAdd.setType(1); // 1=增加余额
                takerAdd.setRemark("订单仲裁赔付，订单号:" + order.getOrderNo());
                takerAdd.setOrderId(order.getId());
                userAccountFeignClient.adjustAccountBalance(takerAdd);
            }

            BigDecimal publisherReturn = order.getPrice().subtract(payAmount);
            if (publisherReturn.compareTo(BigDecimal.ZERO) > 0) {
                AccountAdjustDTO publisherReturnDto = new AccountAdjustDTO();
                publisherReturnDto.setUserId(order.getPublisherId());
                publisherReturnDto.setAmount(publisherReturn);
                publisherReturnDto.setType(1);
                publisherReturnDto.setRemark("订单仲裁返还，订单号:" + order.getOrderNo());
                publisherReturnDto.setOrderId(order.getId());
                userAccountFeignClient.adjustAccountBalance(publisherReturnDto);
            }

            // 解冻并扣除接单人全部保证金（totalDeposit），再按仲裁结果分配：
            AccountAdjustDTO takerDepositUnfreeze = new AccountAdjustDTO();
            takerDepositUnfreeze.setUserId(order.getTakerId());
            takerDepositUnfreeze.setAmount(order.getSecurityDeposit().add(order.getEfficiencyDeposit())); // amount = totalDeposit
            takerDepositUnfreeze.setType(8); // 8=解冻并扣除冻结金额
            takerDepositUnfreeze.setRemark("订单仲裁保证金结算，订单号:" + order.getOrderNo());
            takerDepositUnfreeze.setOrderId(order.getId());
            userAccountFeignClient.adjustAccountBalance(takerDepositUnfreeze);

            if (depositAmount.compareTo(BigDecimal.ZERO) > 0) {
                AccountAdjustDTO publisherAddDeposit = new AccountAdjustDTO();
                publisherAddDeposit.setUserId(order.getPublisherId());
                publisherAddDeposit.setAmount(depositAmount);
                publisherAddDeposit.setType(1);
                publisherAddDeposit.setRemark("订单仲裁赔付保证金，订单号:" + order.getOrderNo());
                publisherAddDeposit.setOrderId(order.getId());
                userAccountFeignClient.adjustAccountBalance(publisherAddDeposit);
            }

            BigDecimal takerReturnDeposit = order.getSecurityDeposit().add(order.getEfficiencyDeposit()).subtract(depositAmount);
            if (takerReturnDeposit.compareTo(BigDecimal.ZERO) > 0) {
                AccountAdjustDTO takerReturnDepositDto = new AccountAdjustDTO();
                takerReturnDepositDto.setUserId(order.getTakerId());
                takerReturnDepositDto.setAmount(takerReturnDeposit);
                takerReturnDepositDto.setType(1);
                takerReturnDepositDto.setRemark("订单仲裁返还保证金，订单号:" + order.getOrderNo());
                takerReturnDepositDto.setOrderId(order.getId());
                userAccountFeignClient.adjustAccountBalance(takerReturnDepositDto);
            }
        }
        // 通知双方
        if (order.getPublisherId() != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(order.getPublisherId());
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已仲裁处理");
            msgDto.setMessageType(1);
            msgDto.setSenderType(3);
            messageServices.sendMessage(msgDto, null);
        }
        if (order.getTakerId() != null) {
            SendMessageDTO msgDto = new SendMessageDTO();
            msgDto.setReceiverId(order.getTakerId());
            msgDto.setOrderId(orderId);
            msgDto.setContent("订单" + order.getOrderNo() + "已仲裁处理");
            msgDto.setMessageType(1);
            msgDto.setSenderType(3);
            messageServices.sendMessage(msgDto, null);
        }
        return R.success("订单已仲裁处理");
    }

    @Override
    public List<GameOrderDistributionVO> getGameOrderDistribution(Integer days) {
        return ordersMapper.getGameOrderDistribution(days);
    }

    @Override
    public List<IncomeTrendVO> getIncomeTrend(Integer days) {
        return ordersMapper.getIncomeTrend(days);
    }

    @Override
    public List<OrderStatusPieVO> getOrderStatusPie(Integer days) {
        return ordersMapper.getOrderStatusPie(days);
    }

} 

