package helper;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.EnumSet;

/**
 * 牌稀有度筛选器，支持一次选择多个稀有度。
 * <p>
 * 本模组只在基础随机牌池中抽牌，因此可选稀有度被限定为
 * {@link AbstractCard.CardRarity#COMMON}、{@link AbstractCard.CardRarity#UNCOMMON}
 * 与 {@link AbstractCard.CardRarity#RARE} 三种；基础、特殊与诅咒稀有度不允许加入筛选器。
 * <p>
 * 实例创建后不可变，可以安全地作为 Power 的 final 字段长期保存。
 */
public final class CardRarityFilter {
    private static final AbstractCard.CardRarity[] ALL_RARITIES = {
            AbstractCard.CardRarity.COMMON,
            AbstractCard.CardRarity.UNCOMMON,
            AbstractCard.CardRarity.RARE
    };

    // “不限制稀有度”的共享实例，等价于三种稀有度全部选中
    public static final CardRarityFilter ANY = new CardRarityFilter(allRarities());
    // 当前筛选器允许的稀有度集合
    private final EnumSet<AbstractCard.CardRarity> allowedRarities;

    // 私有构造：只允许通过 of(...) 或 ANY 创建，保证集合内容始终合法
    private CardRarityFilter(EnumSet<AbstractCard.CardRarity> allowedRarities) {
        this.allowedRarities = allowedRarities;
    }

    /**
     * 创建一个允许若干稀有度的筛选器。
     * 传入的稀有度会自动去重，重复传入不会报错。
     *
     * @param rarities 允许的稀有度，只能是 COMMON、UNCOMMON、RARE
     * @return 对应的筛选器
     */
    public static CardRarityFilter of(AbstractCard.CardRarity... rarities) {
        EnumSet<AbstractCard.CardRarity> selected = EnumSet.noneOf(AbstractCard.CardRarity.class);
        for (AbstractCard.CardRarity rarity : rarities) {
            if (!isSupported(rarity)) {
                throw new IllegalArgumentException("cardRarity 仅支持 COMMON、UNCOMMON、RARE，非法值：" + rarity);
            }
            selected.add(rarity);
        }
        if (selected.isEmpty()) {
            throw new IllegalArgumentException("cardRarity 至少需要选择一种稀有度");
        }
        // 用校验后的集合创建不可变筛选器
        return new CardRarityFilter(selected);
    }

    /**
     * 返回“不限制稀有度”的共享筛选器。
     */
    public static CardRarityFilter any() {
        // 直接复用静态实例，避免重复创建
        return ANY;
    }

    /**
     * 判断指定稀有度是否被当前筛选器允许。
     */
    public boolean contains(AbstractCard.CardRarity rarity) {
        // null 不属于任何集合，直接判定为不匹配
        if (rarity == null) {
            return false;
        }
        // EnumSet 的 contains 为 O(1)
        return this.allowedRarities.contains(rarity);
    }

    /**
     * 判断当前筛选器是否等价于“不限制稀有度”。
     */
    public boolean isAny() {
        // 三种可选稀有度全部在集合中时，就不存在任何限制
        return this.allowedRarities.size() == ALL_RARITIES.length;
    }

    /**
     * 生成用于 Power 描述的中文片段（不含末尾的“牌”字）。
     * <p>
     * 一种稀有度时返回名称，如“普通”；
     * 两种稀有度时返回“非XX”，如“非稀有”；
     * 三种稀有度时返回“随机”。
     */
    public String getDisplayName() {
        // 三种全选等价于不限制，沿用原来的“随机”描述
        if (this.isAny()) {
            return "随机";
        }
        // 只选一种时直接返回该稀有度的名称
        if (this.allowedRarities.size() == 1) {
            // size 为 1 时迭代器一定存在下一个元素
            return getCardRarityName_zh(this.allowedRarities.iterator().next());
        }
        // 只选两种时，等价于排除剩下的第三种
        for (AbstractCard.CardRarity rarity : ALL_RARITIES) {
            // 找出唯一不在集合中的稀有度
            if (!this.allowedRarities.contains(rarity)) {
                // 返回“非 + 被排除稀有度”，例如“非稀有”；末尾的“牌”由本地化文本补全
                return "非" + getCardRarityName_zh(rarity);
            }
        }
        // 理论上是不可达分支，作为兜底仍然返回“随机”
        return "随机";
    }

    // 判断传入的稀有度是否属于本筛选器支持的三种稀有度
    private static boolean isSupported(AbstractCard.CardRarity rarity) {
        // 只有普通、罕见、稀有会出现在基础随机牌池中
        return rarity == AbstractCard.CardRarity.COMMON
                || rarity == AbstractCard.CardRarity.UNCOMMON
                || rarity == AbstractCard.CardRarity.RARE;
    }

    // 构造三种可选稀有度的全集
    private static EnumSet<AbstractCard.CardRarity> allRarities() {
        // 不能直接用 EnumSet.allOf，否则会把 BASIC、SPECIAL 与 CURSE 也算进来
        EnumSet<AbstractCard.CardRarity> set = EnumSet.noneOf(AbstractCard.CardRarity.class);
        // 依次加入三种可选稀有度
        for (AbstractCard.CardRarity rarity : ALL_RARITIES) {
            set.add(rarity);
        }
        // 返回构造好的全集
        return set;
    }

    private static String getCardRarityName_zh(AbstractCard.CardRarity rarity) {
        // 未指定稀有度时按“随机”处理
        if (rarity == null) return "随机";
        // 根据枚举值返回对应的中文名称
        switch (rarity) {
            case BASIC:
                return "基础";
            case COMMON:
                return "普通";
            case UNCOMMON:
                return "罕见";
            case RARE:
                return "稀有";
            case SPECIAL:
                return "特殊";
            case CURSE:
                return "诅咒";
            default:
                return "随机";
        }
    }
}
