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
                return "(将会回复 #g" + (h) + " 点血量！)";
            }else{
                return "(将会失去 #r" + (-h) + " 点血量！)";
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

    // 判断_t是否位于[_s, _e]中
    public static boolean isMiddle(int _t, int _s, int _e){
        return (_t >= _s) && (_t <= _e);
    }

    // ModULimbBasePower随机池
    // 各子类实例化参数都不一样，还需要实时的对象实例，大大方方硬编码了
    public static void ModULimbBasePowerPool(AbstractCreature owner, boolean is_all){
        int r = AbstractDungeon.miscRng.random(1, 100);
        if (is_all && isMiddle(r, 1, 30)){
            // 稀有模肢，概率高点给点存在感
            int nr = AbstractDungeon.miscRng.random(1, 1000);
            if (isMiddle(nr, 1, 120)){
                // 模肢:喙口 击杀敌人时回复生命并获得模肢:冗痕（稀有模肢）
                int cr = AbstractDungeon.miscRng.random(1, 8);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new RostralPortPower(owner, cr, AbstractDungeon.miscRng.random(1, 4)), cr));
            }else if (isMiddle(nr, 121, 300)) {
                // 模肢:权变态算核 回合开始时获得随机模肢并在手牌加入随机牌
                int amount = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new ContingentMorphoComputationalKernelPower(owner, amount,
                                RandomCardHelper.PilePosition.HAND, 1,
                                CardTypeFilter.any(), CardRarityFilter.any()), amount));
            } else if (isMiddle(nr, 301, 600)) {
                // 模肢:叶绿体 每打出若干张牌获得能量
                // amount 需与 cardC 相同，表示距离下次获得能量的剩余牌数
                int cardC = AbstractDungeon.miscRng.random(8, 10);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new ChloroplastPower(owner, cardC, cardC, 1), cardC));
            } else if (isMiddle(nr, 601, 700)) {
                // 模肢:谐枢 获得模肢:冗痕时获得层数点格挡
                int amount = AbstractDungeon.miscRng.random(2, 4);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new HarmonicHubPower(owner, amount), amount));
            } else if (isMiddle(nr, 701, 1000)) {
                // 模肢:忆栈核 每打出一张能力牌获得能量
                int amount = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                        new MnemonicStackKernelPower(owner, amount), amount));
            }

        }else if (is_all && isMiddle(r, 31, 50)){
            // 普通模肢的高数值版：与普通模肢使用相同的候选 Power，仅构造参数更优
            ApplyNormalModULimbPower(owner, true);
        }else if (isMiddle(r, 51, 99)){
            // 普通模肢
            ApplyNormalModULimbPower(owner, false);
        }else{
            // 大失败
            // 模肢:冗痕 战斗结束时失去生命（大失败）
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                    new RedundantVestigePower(owner, 1), 1));
        }
    }

    // 普通模肢与“普通模肢的高数值版”共用同一组候选 Power
    // high 为 true 时只提高构建参数，不改变可能出现的 Power 种类
    private static void ApplyNormalModULimbPower(AbstractCreature owner, boolean high){
        int nr = AbstractDungeon.miscRng.random(1, 1000);
        if (isMiddle(nr, 1, 400)){
            // 模肢:共识棘矛 打出攻击牌时伤害随机敌人
            int amount = 1;
            int damage = high
                    ? AbstractDungeon.miscRng.random(15, 25)
                    : AbstractDungeon.miscRng.random(1, 20);
            int daze = high ? 2 : AbstractDungeon.miscRng.random(1, 2);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                    new ConsensusSpineLancePower(owner, amount, damage, daze), amount));
        }else if (isMiddle(nr, 401, 800)){
            // 模肢:溶注腺 打出攻击牌时给目标附加中毒
            int amount = high
                    ? AbstractDungeon.miscRng.random(2, 3)
                    : AbstractDungeon.miscRng.random(1, 3);
            int poison = high
                    ? AbstractDungeon.miscRng.random(2, 6)
                    : AbstractDungeon.miscRng.random(1, 4);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                    new LysosomalInjectionGlandPower(owner, amount, poison), amount));
        }else if (isMiddle(nr, 801, 1000)){
            // 模肢:权变多能元 打出牌时抽牌，耗尽后获得模肢:冗痕
            int amount = high
                    ? AbstractDungeon.miscRng.random(2, 3)
                    : AbstractDungeon.miscRng.random(1, 2);
            int draw = high
                    ? AbstractDungeon.miscRng.random(2, 3)
                    : AbstractDungeon.miscRng.random(1, 2);
            int vestige = high ? 1 : AbstractDungeon.miscRng.random(1, 2);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
                    new ContingentMultipotentMetaUnitPower(owner, amount, draw, vestige), amount));
        }
    }
}
