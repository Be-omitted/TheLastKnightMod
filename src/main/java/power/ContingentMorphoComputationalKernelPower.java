package power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import helper.CardRarityFilter;
import helper.CardTypeFilter;
import helper.RandomCardHelper;
import helper.TheLastKnightModHelper;

public class ContingentMorphoComputationalKernelPower extends ModULimbBasePower {
    public static final String BASE_ID =
            TheLastKnightModHelper.HEAD_ID + "ContingentMorphoComputationalKernelPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    // 牌堆位置复用 helper 中的定义
    private final RandomCardHelper.PilePosition pilePosition;
    private final int cardCount;
    private final CardTypeFilter cardType;
    private final CardRarityFilter cardRarity;

    /* cardType、cardRarity 填入 null 表示不限制，但抽取范围仍限定在三种基础类型/稀有度内 */
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
