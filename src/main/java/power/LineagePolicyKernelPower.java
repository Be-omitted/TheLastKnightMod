package power;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import helper.TheLastKnightModHelper;

/**
 * 模肢:谱策核
 * 仅作为开关使用：拥有该 Power 时，随机模肢允许出现更稀有的结果。
 */
public class LineagePolicyKernelPower extends ModULimbBasePower {
    public static final String BASE_ID = TheLastKnightModHelper.HEAD_ID + "LineagePolicyKernelPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(BASE_ID);

    /**
     * 创建仅作为开关的谱策核，层数固定为 -1 以隐藏层数显示。
     */
    public LineagePolicyKernelPower(AbstractCreature owner) {
        // 层数只表示“已获得”，使用 -1 隐藏层数显示
        super(owner, -1, "LineagePolicyKernelPower");
        this.ID = BASE_ID;
        this.name = powerStrings.NAME;
        this.type = PowerType.BUFF;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        // 该 Power 只作为开关，重复获得不会叠加效果
        this.fontScale = 8.0F;
        this.updateDescription();
    }

    @Override
    public int getMULNumber() {
        return 1;
    }
}
