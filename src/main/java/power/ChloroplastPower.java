package power;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helper.PowerHistory;
import helper.TheLastKnightModHelper;

/**
 * 模肢:叶绿体
 * 每打出 cardC 张牌，获得 eC 点能量。
 */
public class ChloroplastPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "ChloroplastPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    // History 为空时使用的默认牌数与能量数
    private static final int DEFAULT_CARD_COUNT = 10;
    private static final int DEFAULT_ENERGY = 1;

    // 牌数
    private final int cardc;
    // 能量数
    private final int ec;

    /**
     * @param amount 应与cardC相同
     * @param cardC 单次所需牌数
     * @param eC 单次获取能量数
     */
    public ChloroplastPower(AbstractCreature owner, int amount, int cardC, int eC) {
        super(owner, amount, "ChloroplastPower");
        // 多个叶绿体各自独立计数
        this.ID = BASE_ID + TheLastKnightModHelper.GetPowerNumber(BASE_ID);
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.cardc = Math.max(cardC, 1);
        this.ec = eC;
        this.updateDescription();
    }

    /**
     * undomod 等按 (owner, amount) 反射重建时使用，额外参数从 History 取。
     */
    public ChloroplastPower(AbstractCreature owner, int amount) {
        this(owner, amount, popHistory());
    }

    private ChloroplastPower(AbstractCreature owner, int amount, int[] history) {
        this(owner, amount, history[0], history[1]);
    }

    private static int[] popHistory() {
        Object[] data = PowerHistory.pop(ChloroplastPower.class);
        if (data == null) {
            return new int[]{DEFAULT_CARD_COUNT, DEFAULT_ENERGY};
        }
        return new int[]{(Integer) data[0], (Integer) data[1]};
    }

    @Override
    public void onRemove() {
        PowerHistory.push(ChloroplastPower.class, this.cardc, this.ec);
        super.onRemove();
    }

    @Override
    public void updateDescription() {
        StringBuilder e = new StringBuilder();
        for (int i=0;i<this.ec;i++){
            e.append(" [E] ");
        }
        this.description = powerStrings.DESCRIPTIONS[0] + Math.max(this.cardc, 0)
                + powerStrings.DESCRIPTIONS[1] + e
                + powerStrings.DESCRIPTIONS[2] + this.amount
                + powerStrings.DESCRIPTIONS[3];
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card == null || this.amount <= 0) {
            return;
        }

        if ((this.amount-1) <= 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.ec));
            // 触发后重新开始计数
            this.amount = this.cardc;
        }else this.amount--;
        this.updateDescription();
    }

    @Override
    public int getMULNumber() {
        return 1;
    }
}
