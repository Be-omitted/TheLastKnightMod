package helper;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.EnumSet;

/**
 * 牌类型筛选器，支持一次选择多个牌类型。
 * <p>
 * 本模组只在基础随机牌池中抽牌，因此可选类型被限定为
 * {@link AbstractCard.CardType#ATTACK}、{@link AbstractCard.CardType#SKILL}
 * 与 {@link AbstractCard.CardType#POWER} 三种；状态牌与诅咒牌不会随机生成，
 * 不允许加入筛选器。
 * <p>
 * 实例创建后不可变，可以安全地作为 Power 的 final 字段长期保存。
 */
public final class CardTypeFilter {
    // 允许选择的牌类型全集，同时决定“非XX”描述中排除项的遍历顺序
    private static final AbstractCard.CardType[] ALL_TYPES = {
            AbstractCard.CardType.ATTACK,
            AbstractCard.CardType.SKILL,
            AbstractCard.CardType.POWER
    };

    // “不限制类型”的共享实例，等价于三种类型全部选中
    public static final CardTypeFilter ANY = new CardTypeFilter(allTypes());

    // 当前筛选器允许的牌类型集合
    private final EnumSet<AbstractCard.CardType> allowedTypes;

    // 私有构造：只允许通过 of(...) 或 ANY 创建，保证集合内容始终合法
    private CardTypeFilter(EnumSet<AbstractCard.CardType> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    /**
     * 创建一个允许若干牌类型的筛选器。
     * 传入的类型会自动去重，重复传入不会报错。
     *
     * @param types 允许的牌类型，只能是 ATTACK、SKILL、POWER
     * @return 对应的筛选器
     */
    public static CardTypeFilter of(AbstractCard.CardType... types) {
        // 用一个空集合承接调用方传入的类型
        EnumSet<AbstractCard.CardType> selected = EnumSet.noneOf(AbstractCard.CardType.class);
        // 逐个检查传入的类型
        for (AbstractCard.CardType type : types) {
            // null、状态牌、诅咒牌都不在基础随机牌池内，直接拒绝
            if (!isSupported(type)) {
                throw new IllegalArgumentException("cardType 仅支持 ATTACK、SKILL、POWER，非法值：" + type);
            }
            // 加入集合，重复项由 EnumSet 自动去重
            selected.add(type);
        }
        // 空集合既无法描述也无法抽牌，必须至少选择一种
        if (selected.isEmpty()) {
            throw new IllegalArgumentException("cardType 至少需要选择一种类型");
        }
        // 用校验后的集合创建不可变筛选器
        return new CardTypeFilter(selected);
    }

    /**
     * 返回“不限制牌类型”的共享筛选器。
     */
    public static CardTypeFilter any() {
        // 直接复用静态实例，避免重复创建
        return ANY;
    }

    /**
     * 判断指定牌类型是否被当前筛选器允许。
     */
    public boolean contains(AbstractCard.CardType type) {
        // null 不属于任何集合，直接判定为不匹配
        if (type == null) {
            return false;
        }
        // EnumSet 的 contains 为 O(1)
        return this.allowedTypes.contains(type);
    }

    /**
     * 判断当前筛选器是否等价于“不限制类型”。
     */
    public boolean isAny() {
        // 三种可选类型全部在集合中时，就不存在任何限制
        return this.allowedTypes.size() == ALL_TYPES.length;
    }

    /**
     * 生成用于 Power 描述的中文片段（不含末尾的“牌”字）。
     * <p>
     * 一种类型时返回类型名，如“攻击”；
     * 两种类型时返回“非XX”，如“非能力”；
     * 三种类型时返回“随机”。
     */
    public String getDisplayName() {
        // 三种全选等价于不限制，沿用原来的“随机”描述
        if (this.isAny()) {
            return "随机";
        }
        // 只选一种时直接返回该类型的名称
        if (this.allowedTypes.size() == 1) {
            // size 为 1 时迭代器一定存在下一个元素
            return getCardTypeName_zh(this.allowedTypes.iterator().next());
        }
        // 只选两种时，等价于排除剩下的第三种
        for (AbstractCard.CardType type : ALL_TYPES) {
            // 找出唯一不在集合中的类型
            if (!this.allowedTypes.contains(type)) {
                // 返回“非 + 被排除类型”，例如“非能力”；末尾的“牌”由本地化文本补全
                return "非" + getCardTypeName_zh(type);
            }
        }
        // 理论上是不可达分支，作为兜底仍然返回“随机”
        return "随机";
    }

    // 判断传入的牌类型是否属于本筛选器支持的三种类型
    private static boolean isSupported(AbstractCard.CardType type) {
        // 只有攻击、技能、能力会出现在普通/罕见/稀有随机牌池中
        return type == AbstractCard.CardType.ATTACK
                || type == AbstractCard.CardType.SKILL
                || type == AbstractCard.CardType.POWER;
    }

    // 构造三种可选类型的全集
    private static EnumSet<AbstractCard.CardType> allTypes() {
        // 不能直接用 EnumSet.allOf，否则会把 STATUS 与 CURSE 也算进来
        EnumSet<AbstractCard.CardType> set = EnumSet.noneOf(AbstractCard.CardType.class);
        // 依次加入三种可选类型
        for (AbstractCard.CardType type : ALL_TYPES) {
            set.add(type);
        }
        // 返回构造好的全集
        return set;
    }

    // 原 TheLastKnightModHelper.getCardTypeName_zh，迁移到筛选器内部
    private static String getCardTypeName_zh(AbstractCard.CardType type) {
        // 未指定类型时按“随机”处理
        if (type == null) return "随机";
        // 根据枚举值返回对应的中文名称
        switch (type) {
            case ATTACK:
                return "攻击";
            case SKILL:
                return "技能";
            case POWER:
                return "能力";
            case STATUS:
                return "状态";
            case CURSE:
                return "诅咒";
            default:
                return "随机";
        }
    }
}
