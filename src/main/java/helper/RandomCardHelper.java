package helper;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * 随机抽牌与放入牌堆的工具类。
 * <p>
 * 抽牌范围限定在普通、罕见、稀有三种基础牌池内；类型与稀有度用
 * {@link CardTypeFilter} 与 {@link CardRarityFilter} 描述，传 {@code null} 表示不限制。
 * 本类无状态，所有方法均为静态方法，可直接复用于 Power、卡牌等场景。
 */
public final class RandomCardHelper {

    // 牌堆位置
    public enum PilePosition {
        // 抽牌堆
        DRAW_PILE("抽牌堆", "draw pile"),
        // 弃牌堆
        DISCARD_PILE("弃牌堆", "discard pile"),
        // 手牌
        HAND("手牌", "hand");

        public final String displayName;
        private final String displayNameEn;

        PilePosition(String displayName, String displayNameEn) {
            this.displayName = displayName;
            this.displayNameEn = displayNameEn;
        }

        // 中文使用中文牌堆名，其它语言使用英文牌堆名
        public String getDisplayName() {
            if (Settings.language == Settings.GameLanguage.ZHS
                    || Settings.language == Settings.GameLanguage.ZHT) {
                return this.displayName;
            }
            return this.displayNameEn;
        }
    }

    // 工具类不允许实例化
    private RandomCardHelper() {
    }

    /**
     * 从基础牌池中按类型与稀有度随机取一张牌的副本。
     *
     * @param cardType   牌类型筛选器，null 表示不限制
     * @param cardRarity 稀有度筛选器，null 表示不限制
     * @return 随机牌的副本；没有任何候选牌时返回 null
     */
    public static AbstractCard getRandomCard(CardTypeFilter cardType, CardRarityFilter cardRarity) {
        // 统一在此处理 null，调用方无需重复判空
        CardTypeFilter typeFilter = (cardType == null) ? CardTypeFilter.any() : cardType;
        CardRarityFilter rarityFilter = (cardRarity == null) ? CardRarityFilter.any() : cardRarity;

        // 收集符合条件的牌；包含多稀有度，因此不使用 AbstractDungeon.getCard
        ArrayList<AbstractCard> candidates = new ArrayList<>();
        collectCandidates(candidates, AbstractDungeon.commonCardPool, typeFilter, rarityFilter);
        collectCandidates(candidates, AbstractDungeon.uncommonCardPool, typeFilter, rarityFilter);
        collectCandidates(candidates, AbstractDungeon.rareCardPool, typeFilter, rarityFilter);

        if (candidates.isEmpty()) {
            return null;
        }
        // 返回副本，避免调用方改动牌池中的原牌
        int index = AbstractDungeon.cardRng.random(candidates.size() - 1);
        return candidates.get(index).makeCopy();
    }

    /**
     * 随机抽取 count 张牌并逐张放入指定牌堆。
     *
     * @return 实际放入的张数，候选牌不足时会提前停止
     */
    public static int addRandomCardsToPile(PilePosition pilePosition, int count,
                                           CardTypeFilter cardType, CardRarityFilter cardRarity) {
        int added = 0;
        for (int i = 0; i < count; i++) {
            AbstractCard card = getRandomCard(cardType, cardRarity);
            if (card == null) {
                break;
            }
            addCardToPile(card, pilePosition);
            added++;
        }
        return added;
    }

    /**
     * 随机抽取一张牌并放入指定牌堆。
     *
     * @return 是否成功放入；没有候选牌时返回 false
     */
    public static boolean addRandomCardToPile(PilePosition pilePosition,
                                              CardTypeFilter cardType, CardRarityFilter cardRarity) {
        AbstractCard card = getRandomCard(cardType, cardRarity);
        if (card == null) {
            return false;
        }
        addCardToPile(card, pilePosition);
        return true;
    }

    /**
     * 把已有卡牌放入指定牌堆。
     */
    public static void addCardToPile(AbstractCard card, PilePosition pilePosition) {
        // 入参不完整时直接忽略，避免向 actionManager 提交无效动作
        if (card == null || pilePosition == null) {
            return;
        }
        switch (pilePosition) {
            case DRAW_PILE:
                // 抽牌堆
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(card, 1, true, false));
                break;
            case DISCARD_PILE:
                // 弃牌堆
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, 1));
                break;
            case HAND:
                // 手牌
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
                break;
            default:
                break;
        }
    }

    // 把牌池中同时满足类型与稀有度条件的牌加入候选列表
    private static void collectCandidates(ArrayList<AbstractCard> candidates, CardGroup pool,
                                          CardTypeFilter cardType, CardRarityFilter cardRarity) {
        if (pool == null || pool.group == null) {
            return;
        }
        for (AbstractCard card : pool.group) {
            // 两项条件都满足才加入候选
            if (cardType.contains(card.type) && cardRarity.contains(card.rarity)) {
                candidates.add(card);
            }
        }
    }
}
