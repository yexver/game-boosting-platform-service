package com.jmz.serverorder.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.MessagesQueryDTO;
import com.jmz.serverorder.dto.SendMessageDTO;
import com.jmz.serverorder.service.MessageServices;
import com.jmz.serverorder.vo.SendMessageResponseVO;
import com.jmz.serverorder.vo.UnreadCountVO;
import com.jmz.serveruser.vo.UserInfoVo;
import com.jmz.serverorder.entity.Orders;
import com.jmz.serverorder.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private MessageServices messageService;

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 获取消息列表（按用户分组，显示最后一条消息）
     */
    @GetMapping("/list")
    public R getMessageList(MessagesQueryDTO queryDTO) {
        return R.success(messageService.getMessageList(queryDTO));
    }

    /**
     * 获取聊天用户信息
     * @param userId 聊天对象的用户ID（对方用户ID）
     */
    @GetMapping("/chat-user/{userId}")
    public R getChatUserInfo(@PathVariable Long userId) {
        UserInfoVo userInfo = messageService.getChatUserInfo(userId);
        return R.success(userInfo);
    }

    /**
     * 获取聊天消息历史
     */
    @GetMapping({"/chat", "/chat/{userId}"})
    public R getChatHistory(@PathVariable(required = false) Long userId,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "20") Integer pageSize) {
        if (userId == null || userId <= 0) {
            return R.success(messageService.getSystemHistory(page, pageSize));
        } else {
            return R.success(messageService.getChatHistory(userId, page, pageSize));
        }
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R sendMessage(@RequestParam Long receiverId,
                        @RequestParam(required = false) String content,
                        @RequestParam(required = false) Integer senderType,
                        @RequestParam Integer messageType,
                        @RequestParam(required = false) Long orderId,
                        @RequestParam(required = false) MultipartFile[] images) {
        
        SendMessageDTO dto = new SendMessageDTO();
        dto.setReceiverId(receiverId);
        dto.setContent(content);
        dto.setSenderType(senderType);
        dto.setMessageType(messageType);
        dto.setOrderId(orderId);

        SendMessageResponseVO response = messageService.sendMessage(dto, images);

        return R.success(response);
    }

    /**
     * 上传订单图片并创建消息
     */
    @PostMapping("/upload-images")
    public R uploadOrderImages(@RequestParam Long orderId,
                               @RequestParam String type,
                               @RequestParam(value = "senderType") Integer senderType,
                               @RequestParam("images") MultipartFile[] images) {
        // 1. 上传图片，获取图片路径列表
        // 2. 创建一条消息记录，内容为type，图片为上传后的路径
        SendMessageDTO dto = new SendMessageDTO();
        dto.setOrderId(orderId);
        dto.setContent(type); // 消息内容为图片类型
        dto.setMessageType(2); // 2为图片消息类型，根据实际业务调整
        dto.setSenderType(senderType);
        // 查找订单，获取发单者id
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }

        dto.setReceiverId(order.getPublisherId());
        SendMessageResponseVO response = messageService.sendMessage(dto, images);

        return R.success(response);
    }

    /**
     * 标记消息已读
     */
    @PutMapping("/read/{messageId}")
    public R markMessageRead(@PathVariable Long messageId) {
        messageService.markMessageRead(messageId);
        return R.success();
    }

    /**
     * 标记所有消息已读
     */
    @PutMapping("/read-all")
    public R markAllMessagesRead() {
        Map<String, Object> result = messageService.markAllMessagesRead();
        return R.success(result);
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread-count")
    public R getUnreadCount() {
        UnreadCountVO result = messageService.getUnreadCount();
        return R.success(result);
    }

    /**
     * 获取订单消息列表
     */
    @GetMapping("/order-messages")
    public R getOrderMessages(@RequestParam Long orderId) {
        return R.success(messageService.getOrderMessagesByOrderId(orderId));
    }
} 