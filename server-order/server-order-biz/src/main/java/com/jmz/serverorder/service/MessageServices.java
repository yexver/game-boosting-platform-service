package com.jmz.serverorder.service;

import com.jmz.serverorder.dto.MessagesQueryDTO;
import com.jmz.serverorder.dto.SendMessageDTO;
import com.jmz.serverorder.vo.ChatMessageVO;
import com.jmz.serverorder.vo.SendMessageResponseVO;
import com.jmz.serverorder.vo.UnreadCountVO;
import com.jmz.serveruser.vo.UserInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MessageServices {
    /**
     * 获取消息列表（按用户分组，显示最后一条消息）
     */
    Map<String, Object> getMessageList(MessagesQueryDTO queryDTO);

    /**
     * 获取聊天用户信息
     */
    UserInfoVo getChatUserInfo(Long userId);

    /**
     * 获取聊天消息历史
     */
    Map<String, Object> getChatHistory(Long userId, Integer page, Integer pageSize);

    /**
     * 发送消息
     */
    SendMessageResponseVO sendMessage(SendMessageDTO dto, MultipartFile[] images);

    /**
     * 标记消息已读
     */
    void markMessageRead(Long messageId);

    /**
     * 标记所有消息已读
     */
    Map<String, Object> markAllMessagesRead();

    /**
     * 获取未读消息数量
     */
    UnreadCountVO getUnreadCount();

    /**
     * 获取聊天消息历史
     */
    Map<String, Object> getSystemHistory(Integer page, Integer pageSize);

    /**
     * 获取订单消息列表（不分页）
     */
    List<ChatMessageVO> getOrderMessagesByOrderId(Long orderId);
}