package character;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import cards.Attack.Strike_TheLastKnight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import core.TheLastKnightModCore;
import pathes.ThmodClassEnum;
import pathes.AbstractCardEnum;
import power.ChargeFormPower;
import relics.BreathOfTheTide;

import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.core.CardCrawlGame.psb;

public class TheLastKnight extends CustomPlayer{
    //人物介绍
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("TheLastKnightMod:TheLastKnight");;
    //初始能量
    private static final int ENERGY_PER_TURN = 3;
    //篝火休息处的角色背影
    private static final String SELES_SHOULDER_1 = "img/char/shoulder.png";
    private static final String SELES_SHOULDER_2 = "img/char/shoulder.png";
    //能量球素材
    private static final String[] ORB_TEXTURES = new String[] {"img/UI/orb/layer5.png", "img/UI/orb/layer4.png", "img/UI/orb/layer3.png", "img/UI/orb/layer2.png", "img/UI/orb/layer1.png", "img/UI/orb/layer6.png", "img/UI/orb/layer5d.png", "img/UI/orb/layer4d.png", "img/UI/orb/layer3d.png", "img/UI/orb/layer2d.png", "img/UI/orb/layer1d.png"};
    private static final String ORB_VFX = "img/UI/orb/vfx.png";
    private static final float[] LAYER_SPEED = new float[] { -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F };
    //初始生命，最大生命，初始金币,初始的充能球栏位（机器人）,进阶14时的最大生命值下降
    private static final int STARTING_HP = 80;
    private static final int MAX_HP = 80;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 15;

    // 记录上一帧看到的冲锋形态实例与层数，用于检测获得/叠加的瞬间
    private AbstractPower chargeFormPower = null;

    // ---------- Spine 模型 ----------
    // 模型包含的动画：Idle / Move / Move_2 / Attack / Default / Die / Skill_1_Begin / Skill_1_End / Skill_1_Loop
    public TheLastKnight(String name) {
        super(name, ThmodClassEnum.TheLastKnight_CLASS, ORB_TEXTURES, ORB_VFX, LAYER_SPEED,
                new SpineAnimation("img/char/enemy_2038_sydonq.atlas", "img/char/enemy_2038_sydonq.json", 1.5F));
        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass((String)null, SELES_SHOULDER_2, SELES_SHOULDER_1, (String)null,
                getLoadout(),
                0.0F, 0.0F, 200.0F, 220.0F,
                new EnergyManager(ENERGY_PER_TURN));
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.2F);
    }

    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        // 每帧按当前 Power 状态校正动画，能适配 undo 或中途失去 Power
        updateChargeFormAnimation();
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
        this.skeleton.setColor(this.tint.color);
        sb.end();
        psb.begin();
        sr.draw(psb, this.skeleton);
        psb.end();
        sb.begin();
    }

    // 实时判断是否拥有冲锋形态，避免缓存状态导致失去 Power 后仍播放特殊动画
    private boolean hasChargeFormPower() {
        return getChargeFormPower() != null;
    }

    // 取当前生效的冲锋形态实例，层数为 0 时视为没有该 Power
    private AbstractPower getChargeFormPower() {
        AbstractPower power = this.getPower(ChargeFormPower.BASE_ID);
        return (power != null && power.amount > 0) ? power : null;
    }

    // 根据冲锋形态状态切换待机动画，并在获得该 Power 时播放一次 Skill_1_End
    private void updateChargeFormAnimation() {
        AbstractPower currentPower = getChargeFormPower();
        boolean hasChargeForm = currentPower != null;

        // 新获得或层数增加时播放 Skill_1_End，结束后回到 Move_2 待机
        if (hasChargeForm
                && (this.chargeFormPower != currentPower)
                && !this.isDying) {
            this.state.setAnimation(0, "Skill_1_End", false);
            this.state.addAnimation(0, "Move_2", true, 0.0F);
        }
        this.chargeFormPower = currentPower;

        // 死亡动画由 playDeathAnimation 控制，这里不再覆盖
        if (this.isDying) {
            return;
        }

        AnimationState.TrackEntry entry = this.state.getCurrent(0);
        if (entry == null || entry.getAnimation() == null) {
            return;
        }
        String current = entry.getAnimation().getName();
        String idle = hasChargeForm ? "Move_2" : "Idle";
        // 只校正待机类动画，避免打断 Skill_1_End 等一次性动画
        if ((current.equals("Idle") || current.equals("Move_2")) && !current.equals(idle)) {
            this.state.setAnimation(0, idle, true);
        }
    }

    @Override
    public void useFastAttackAnimation() {
        // 拥有冲锋形态时保持 Move_2，不切换到攻击动画
        if (hasChargeFormPower()) {
            return;
        }
        this.state.setAnimation(0, "Attack", false).setTimeScale(2.0F);;
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }
    @Override
    public void useSlowAttackAnimation() {
        // 拥有冲锋形态时保持 Move_2，不切换到攻击动画
        if (hasChargeFormPower()) {
            return;
        }
        this.state.setAnimation(0, "Attack", false).setTimeScale(2.0F);
        this.state.addAnimation(0, "Idle", true, 0.0F);
    }

    @Override
    public void playDeathAnimation() {
        this.isDying = true;
        // 执行时再判定，拥有冲锋形态时改用 Die 动画
        if (hasChargeFormPower()) {
            this.state.setAnimation(0, "Die", false);
        } else {
            this.state.setAnimation(0, "Skill_1_Begin", false);
        }
    }
    // 初始化
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("TheLastKnightMod:Strike_TheLastKnight");
        retVal.add("TheLastKnightMod:Strike_TheLastKnight");
        retVal.add("TheLastKnightMod:Strike_TheLastKnight");
        retVal.add("TheLastKnightMod:Strike_TheLastKnight");
        retVal.add("TheLastKnightMod:Defend_TheLastKnight");
        retVal.add("TheLastKnightMod:Defend_TheLastKnight");
        retVal.add("TheLastKnightMod:Defend_TheLastKnight");
        retVal.add("TheLastKnightMod:Defend_TheLastKnight");
        retVal.add("TheLastKnightMod:Charge");
        retVal.add("TheLastKnightMod:FeedingInstinct");

        return retVal;
    }
    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(BreathOfTheTide.ID);
        UnlockTracker.markRelicAsSeen(BreathOfTheTide.ID);
        return retVal;
    }
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0], STARTING_HP, MAX_HP, 0, STARTING_GOLD, 5, this, this.getStartingRelics(), this.getStartingDeck(), false);
    }
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }
    @Override
    public AbstractCard.CardColor getCardColor() { return AbstractCardEnum.TheLastKnight_COLOR;}
    @Override
    public AbstractCard getStartCardForEvent() {return new Strike_TheLastKnight();}
    @Override
    public Color getCardTrailColor() {return TheLastKnightModCore.LIGHT;}
    @Override
    public int getAscensionMaxHPLoss() {
        return ASCENSION_MAX_HP_LOSS;
    }
    @Override
    public BitmapFont getEnergyNumFont() {return FontHelper.energyNumFontRed;}
    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> list = new ArrayList<>();
        list.add(new CutscenePanel("img/char/end1.png"));
        list.add(new CutscenePanel("img/char/end2.png"));
        list.add(new CutscenePanel("img/char/end3.png"));
        return list;
    }
    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }
    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }
    @Override
    public Color getSlashAttackColor(){ return TheLastKnightModCore.LIGHT;}
    // 吸血鬼事件文本
    @Override
    public String getVampireText(){ return null;}
    @Override
    public Color getCardRenderColor(){ return TheLastKnightModCore.LIGHT;}
    @Override
    public void doCharSelectScreenSelectEffect(){
        CardCrawlGame.screenShake.shake(
                ScreenShake.ShakeIntensity.MED,
                ScreenShake.ShakeDur.SHORT,
                true
        );
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey(){return "ATTACK_HEAVY";}
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL};
    }
    @Override
    public AbstractPlayer newInstance(){
        return new TheLastKnight(this.name);
    }
}
