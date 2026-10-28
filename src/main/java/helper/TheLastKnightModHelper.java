package helper;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import power.*;

import java.util.TreeMap;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TheLastKnightModHelper {
    public static final String HEAD_ID = "TheLastKnightMod:";
    private static final TreeMap<String, Integer> PowerNumber = new TreeMap<>();

    public TheLastKnightModHelper(){}

    public static int GetWillH(AbstractPlayer p){
        if (p == null) return 0;

        int HAmount = 0;
        if (p.hasPower(WillPower.BASE_ID) && p.getPower(WillPower.BASE_ID).amount > 0){
            HAmount += p.getPower(WillPower.BASE_ID).amount;
        }
        if (p.hasPower(RedundantVestigePower.BASE_ID)) {
            HAmount -= p.getPower(RedundantVestigePower.BASE_ID).amount;
        }
        // 狂徒妄念：冗痕伤害不会致死，最多把生命扣到 1
        if (HAmount < 0 && p.hasPower(FanaticSDelusionPower.BASE_ID)) {
            HAmount = Math.max(HAmount, -Math.max(0, p.currentHealth - 1));
        }
        return HAmount;
    }
    public static String GetWillHText(AbstractCreature creature){
        if (creature instanceof AbstractPlayer){
            int h = GetWillH((AbstractPlayer) creature);
            if (h>=0){
                return "(将会回复 #g" + String.valueOf(h) + " 点血量！)";
            }else{
                return "(将会失去 #r" + String.valueOf(h) + " 点血量！)";
            }
        }else{
            return "(在其它单位上时无效！)";
        }
    }
    // 获取模肢数
    public static int GetModULimbBasePowerNumber(AbstractCreature creature){
        int MULNumber = 0;
        if (creature == null) return MULNumber;
        for (AbstractPower power : creature.powers){
            if (power instanceof ModULimbBasePower){
                MULNumber += ((ModULimbBasePower) power).getMULNumber();
            }
        }
        return MULNumber;
    }
    // 部分需独立的power更新编号
    public static int GetPowerNumber(String ID){
        if (PowerNumber.containsKey(ID)){
            int tid = PowerNumber.get(ID);
            PowerNumber.put(ID, (tid > 10000)?0:(tid+1));
            return tid;
        }else {
            PowerNumber.put(ID, 1);
            return 0;
        }
    }
    // ModULimbBasePower随机池参数
    public static boolean ModULimbBasePowerPoolIsAll(AbstractCreature owner){
        if (owner == null) return false;
        // 拥有 模肢:谱策核 时，随机模肢允许更稀有的结果
        return owner.hasPower(LineagePolicyKernelPower.BASE_ID);
    }

    // ModULimbBasePower随机池
    // 各子类实例化参数都不一样，还需要实时的对象实例，大大方方硬编码了
    public static void ModULimbBasePowerPool(AbstractCreature owner, boolean is_all){
        int r = AbstractDungeon.miscRng.random(1, 100);
        if (is_all && (r <= 30)){
            // 稀有模肢，概率高点给点存在感
            r = AbstractDungeon.miscRng.random(1, 1);
            if (r == 1){
                r = AbstractDungeon.miscRng.random(1, 8);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new RostralPortPower(owner, r, AbstractDungeon.miscRng.random(1, 4)), r));
                }
        }else if (r <= 99){
            r = AbstractDungeon.miscRng.random(1, 7);
            if (r <= 2){
                r = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new ConsensusSpineLancePower(owner, r, AbstractDungeon.miscRng.random(1, 30), AbstractDungeon.miscRng.random(1, 2)), r));
            }else if (r <= 4){
                r = AbstractDungeon.miscRng.random(1, 4);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new LysosomalInjectionGlandPower(owner, r, AbstractDungeon.miscRng.random(1, 6)), r));
            } else if (r <= 7) {
                r = AbstractDungeon.miscRng.random(1, 4);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new ContingentMultipotentMetaUnitPower(owner, r, AbstractDungeon.miscRng.random(1, 3), AbstractDungeon.miscRng.random(1, 2)), r));
            }
        }else{
            // 大失败
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                    new RedundantVestigePower(owner, 1), 1));
        }
    }
}
