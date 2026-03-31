package com.jmz.serveraccount.enums;

public enum AccountTypeEnum {

    // ========== 收入类 (1-9) ==========
    RECHARGE(1, "充值"),
    INCOME(2, "订单收入"),
    REFUND(3, "退款"),

    // ========== 支出类 (10-19) ==========
    WITHDRAW(10, "提现"),
    EXPENSE(11, "订单支出"),
    FINE(12, "罚款"),

    // ========== 冻结类 (20-29) ==========
    FREEZE(20, "冻结资金"),

    // ========== 解冻/扣减类 (30-39) ==========
    UNFREEZE(30, "解冻资金"),
    FROZEN_DEDUCT(31, "解冻并扣除冻结金额");

    private final int code;
    private final String desc;

    AccountTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountTypeEnum fromCode(int code) {
        for (AccountTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("未知账户类型: " + code);
    }

    /** 是否为扣减类操作（需走乐观锁原子扣款） */
    public boolean isDeduct() {
        return this == WITHDRAW || this == EXPENSE || this == FINE;
    }

    /** 是否为加款类操作 */
    public boolean isAdd() {
        return this == INCOME || this == REFUND || this == RECHARGE || this == UNFREEZE;
    }

    /** 是否为冻结类操作 */
    public boolean isFreeze() {
        return this == FREEZE;
    }

    /** 是否为解冻类操作 */
    public boolean isUnfreeze() {
        return this == UNFREEZE || this == FROZEN_DEDUCT;
    }
}
