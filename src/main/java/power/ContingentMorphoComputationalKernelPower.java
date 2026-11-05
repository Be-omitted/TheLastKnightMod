package power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import helper.PowerHistory;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;

/**
 * 模肢:权变态算核
 * 回合开始时获得随机模肢，并向指定牌堆加入随机牌。
 */
public class ContingentMorphoComputationalKernelPower extends ModULimbBasePower {
    public static final String BASE_ID =
            TheLastKnightModHelper.HEAD_ID + "ContingentMorphoComputationalKernelPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    // History 为空时使用的默认构造参数
    private static final RandomCardHelper.PilePosition DEFAULT_PILE_POSITION =
            RandomCardHelper.PilePosition.HAND;
    private static final int DEFAULT_CARD_COUNT = 1;

    // 牌堆位置复用 helper 中的定义
    private final RandomCardHelper.PilePosition pilePosition;
    private final int cardCount;
    private final CardTypeFilter cardType;
    private final CardRarityFilter cardRarity;

    /**
     * cardType、cardRarity 填入 null 表示不限制，但抽取范围仍限定在三种基础类型/稀有度内。
     *
     * @param limbAmount 层数，同时决定回合开始时获得的随机模肢数量
     * @param pilePosition 随机牌放入的牌堆
     * @param cardCount 每次加入的牌数
     * @param cardType 牌类型筛选器，null 表示不限制
     * @param cardRarity 稀有度筛选器，null 表示不限制
     */
    public ContingentMorphoComputationalKernelPower(AbstractCreature owner, int limbAmount,
                                                    RandomCardHelper.PilePosition pilePosition,
                                                    int cardCount, CardTypeFilter cardType,
                                                    CardRarityFilter cardRarity) {
        super(owner, limbAmount, "ContingentMorphoComputationalKernelPower");
        this.ID = BASE_ID + TheLastKnightModHelper.GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;

        this.pilePosition = pilePosition;
        this.cardCount = cardCount;
        this.cardType = (cardType == null) ? CardTypeFilter.any() : cardType;
        this.cardRarity = (cardRarity == null) ? CardRarityFilter.any() : cardRarity;

        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    /**
     * undomod 等按 (owner, amount) 反射重建时使用，额外参数从 History 取。
     */
    public ContingentMorphoComputationalKernelPower(AbstractCreature owner, int amount) {
        this(owner, amount, popHistory());
    }

    private ContingentMorphoComputationalKernelPower(AbstractCreature owner, int amount, Object[] history) {
        this(owner, amount,
                (RandomCardHelper.PilePosition) history[0],
                (Integer) history[1],
                (CardTypeFilter) history[2],
                (CardRarityFilter) history[3]);
    }

    private static Object[] popHistory() {
        Object[] data = PowerHistory.pop(ContingentMorphoComputationalKernelPower.class);
        if (data != null) {
            return data;
        }
        return new Object[]{DEFAULT_PILE_POSITION, DEFAULT_CARD_COUNT,
                CardTypeFilter.any(), CardRarityFilter.any()};
    }

    @Override
    public void onRemove() {
        PowerHistory.push(ContingentMorphoComputationalKernelPower.class,
                this.pilePosition, this.cardCount, this.cardType, this.cardRarity);
        super.onRemove();
    }

    @Override
    public void updateDescription() {
        // 描述开头：当前层数，负数按 0 显示，避免出现负层数
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.amount, 0)
                + powerStrings.DESCRIPTIONS[1] + this.pilePosition.displayName
                + powerStrings.DESCRIPTIONS[2] + this.cardCount
                + powerStrings.DESCRIPTIONS[3]
                + getCardFilterDescription()
                + powerStrings.DESCRIPTIONS[4];
    }

    // 把稀有度筛选器与类型筛选器的描述拼成一段自然的中文
    private String getCardFilterDescription() {
        // 取稀有度部分，例如“普通”“非稀有”“随机”
        String rarityText = this.cardRarity.getDisplayName();
        // 取类型部分，例如“攻击”“非能力”“随机”
        String typeText = this.cardType.getDisplayName();
        // 两边都不限制时只输出一个“随机”，避免出现“随机随机”
        if (this.cardRarity.isAny() && this.cardType.isAny()) {
            return "随机";
        }
        // 只限制类型时，省略稀有度部分
        if (this.cardRarity.isAny()) {
            return typeText;
        }
        // 只限制稀有度时，省略类型部分
        if (this.cardType.isAny()) {
            return rarityText;
        }
        // 两边都是“非XX”时用“且”连接，例如“非稀有且非能力”
        if (rarityText.startsWith("非") && typeText.startsWith("非")) {
            return rarityText + "且" + typeText;
        }
        // 其余情况沿用原顺序：稀有度在前、类型在后，例如“普通攻击”
        return rarityText + typeText;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount <= 0) {
            return;
        }
        this.flash();

        // 每层效果各触发一次随机模肢抽取
        for (int i = 0; i < this.amount; i++) {
            TheLastKnightModHelper.ModULimbBasePowerPool(this.owner,
                    TheLastKnightModHelper.ModULimbBasePowerPoolIsAll(this.owner));
        }

        // 按配置的张数逐张抽牌并放入目标牌堆，抽牌与放牌逻辑见 RandomCardHelper
        RandomCardHelper.addRandomCardsToPile(this.pilePosition, this.cardCount,
                this.cardType, this.cardRarity);
    }

    @Override
    public int getMULNumber() {
        return 1;
    }

}
