-- ============================================
-- 账户类型枚举更新脚本
-- 执行时间: 2026-03-30
-- ============================================

-- 旧枚举值 -> 新枚举值 映射
-- 1(收入) -> 2(订单收入)
-- 2(充值) -> 1(充值)
-- 3(提现) -> 10(提现)
-- 4(支出) -> 11(订单支出)
-- 5(冻结资金) -> 20(冻结资金)
-- 6(解冻资金) -> 30(解冻资金)

-- 注意: 旧枚举值1(收入)和2(充值)需要特别处理
-- 旧1 -> 新2 (订单收入)
UPDATE tb_jmz_transactions SET type = 2 WHERE type = 1;

-- 旧2 -> 新1 (充值)
UPDATE tb_jmz_transactions SET type = 1 WHERE type = 2;

-- 旧3 -> 新10 (提现)
UPDATE tb_jmz_transactions SET type = 10 WHERE type = 3;

-- 旧4 -> 新11 (订单支出)
UPDATE tb_jmz_transactions SET type = 11 WHERE type = 4;

-- 旧5 -> 新20 (冻结资金)
UPDATE tb_jmz_transactions SET type = 20 WHERE type = 5;

-- 旧6 -> 新30 (解冻资金)
UPDATE tb_jmz_transactions SET type = 30 WHERE type = 6;

-- 旧7 -> 新31 (冻结金额扣除)
UPDATE tb_jmz_transactions SET type = 31 WHERE type = 7;

-- 修改列注释
ALTER TABLE tb_jmz_transactions 
MODIFY COLUMN type TINYINT NOT NULL COMMENT '交易类型：1-充值，2-订单收入，3-退款，10-提现，11-订单支出，12-罚款，20-冻结资金，30-解冻资金，31-解冻并扣除冻结金额';
