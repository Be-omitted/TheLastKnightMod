package core;
import basemod.BaseMod;
import basemod.interfaces.*;
import character.TheLastKnight;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import helper.PowerHistory;
import helper.TheLastKnightModHelper;
import patches.AbstractCardEnum;
import com.badlogic.gdx.graphics.Color;
import patches.ThmodClassEnum;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpireInitializer
public class TheLastKnightModCore
        implements EditCharactersSubscriber, EditStringsSubscriber,
                EditCardsSubscriber, EditRelicsSubscriber,
                EditKeywordsSubscriber, OnStartBattleSubscriber,
                AddAudioSubscriber, PostInitializeSubscriber,
                PostBattleSubscriber {

    // 深蓝基底色
    public static final Color LIGHT = new Color(0x000081FF);
    private ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
    public TheLastKnightModCore(){
        BaseMod.subscribe(this);
        BaseMod.addColor(AbstractCardEnum.TheLastKnight_COLOR, LIGHT, LIGHT, LIGHT, LIGHT, LIGHT, LIGHT, LIGHT, "img/512/attack_512.png", "img/512/skill_512.png", "img/512/power_512.png", "img/char/cost_orb.png", "img/1024/attack.png", "img/1024/skill.png", "img/1024/power.png", "img/char/card_orb.png", "img/char/small_orb.png");
    }
    public static void initialize() {
        new TheLastKnightModCore();
    }
    @Override
    public void receiveEditStrings(){
        // 暂只支持中文
        String lang = "zhs";

        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/" + lang + "/Card.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization/" + lang + "/Power.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/" + lang + "/Character.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/" + lang + "/Relic.json");
    }

    @Override
    public void receiveEditCharacters(){
        BaseMod.addCharacter(new TheLastKnight(CardCrawlGame.playerName), "img/char/Button.png", "img/char/Portrait.png", ThmodClassEnum.TheLastKnight_CLASS);
    }
    @Override
    public void receiveEditCards() {
        loadCardsToAdd();
        for (AbstractCard card : this.cardsToAdd) {
            BaseMod.addCard(card);
            UnlockTracker.markCardAsSeen(card.cardID);
        }
    }
    private void loadCardsToAdd() {
        this.cardsToAdd.clear();
        // 初始卡组内的卡牌
        this.cardsToAdd.add(new cards.Attack.Strike_TheLastKnight());
        this.cardsToAdd.add(new cards.Skill.Defend_TheLastKnight());
        this.cardsToAdd.add(new cards.Attack.Charge());
        this.cardsToAdd.add(new cards.Power.FeedingInstinct());
        // 攻击牌
        this.cardsToAdd.add(new cards.Attack.LightAttack());
        this.cardsToAdd.add(new cards.Attack.Dash());
        this.cardsToAdd.add(new cards.Attack.SweepingSlash());
        this.cardsToAdd.add(new cards.Attack.KazimierzSwordsmanship());
        this.cardsToAdd.add(new cards.Attack.Diligence());
        this.cardsToAdd.add(new cards.Attack.PerishWithTheEnemy());
        this.cardsToAdd.add(new cards.Attack.Tempest());
        this.cardsToAdd.add(new cards.Attack.Combo());
        // 技能牌
        this.cardsToAdd.add(new cards.Skill.ReactiveHyperplasia());
        this.cardsToAdd.add(new cards.Skill.CorrosiveLiquid());
        this.cardsToAdd.add(new cards.Skill.Adaptation());
        this.cardsToAdd.add(new cards.Skill.Manifest());
        this.cardsToAdd.add(new cards.Skill.Humility());
        this.cardsToAdd.add(new cards.Skill.Ecstasy());
        this.cardsToAdd.add(new cards.Skill.EternalBlaze());
        this.cardsToAdd.add(new cards.Skill.Generosity());
        this.cardsToAdd.add(new cards.Skill.Temperance());
        this.cardsToAdd.add(new cards.Skill.Chastity());
        this.cardsToAdd.add(new cards.Skill.StressResponse());
        this.cardsToAdd.add(new cards.Skill.BurnTheOldSelf());
        this.cardsToAdd.add(new cards.Skill.Curse());
        this.cardsToAdd.add(new cards.Skill.DeathSentence());
        this.cardsToAdd.add(new cards.Skill.Lantern());
        this.cardsToAdd.add(new cards.Skill.HelpingHand());
        this.cardsToAdd.add(new cards.Skill.Adjustment());
        this.cardsToAdd.add(new cards.Skill.KnightTraining());
        this.cardsToAdd.add(new cards.Skill.BecomeFire());
        this.cardsToAdd.add(new cards.Skill.HeartOfTheLegion());
        this.cardsToAdd.add(new cards.Skill.BoneCurettage());
        // 能力牌
        this.cardsToAdd.add(new cards.Power.PlannedGrowth());
        this.cardsToAdd.add(new cards.Power.DirectedEvolution());
        this.cardsToAdd.add(new cards.Power.ChargeForm());
        this.cardsToAdd.add(new cards.Power.FanaticSDelusion());
        this.cardsToAdd.add(new cards.Power.Charity());
        this.cardsToAdd.add(new cards.Power.Patience());
        this.cardsToAdd.add(new cards.Power.Hope());
        this.cardsToAdd.add(new cards.Power.ChildhoodSEnd());
        this.cardsToAdd.add(new cards.Power.Photosynthesis());
        this.cardsToAdd.add(new cards.Power.TheUseOfTheUseless());
        this.cardsToAdd.add(new cards.Power.WhatConstitutesTheSelf());
        this.cardsToAdd.add(new cards.Power.DefyDeath());
        // 状态牌
        this.cardsToAdd.add(new cards.Status.Exhaustion());
    }
    private void receiveAddMonster() {
    }
    private void receiveAddEvent() {
    }
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new relics.BreathOfTheTide(), AbstractCardEnum.TheLastKnight_COLOR);
        BaseMod.addRelicToCustomPool(new relics.AbsurdDestiny(), AbstractCardEnum.TheLastKnight_COLOR);
        BaseMod.addRelicToCustomPool(new relics.KnightlyBloodAndBone(), AbstractCardEnum.TheLastKnight_COLOR);
        BaseMod.addRelicToCustomPool(new relics.CodeOfChivalry(), AbstractCardEnum.TheLastKnight_COLOR);
    }
    @Override
    public void receiveAddAudio(){}
    @Override
    public void receivePostInitialize() {
        this.receiveAddMonster();
        this.receiveAddEvent();
        this.receiveAddAudio();

        // 解锁n20
        for (AbstractPlayer _p : BaseMod.getModdedCharacters()){
            if (_p instanceof TheLastKnight){
                Prefs prefs = _p.getPrefs();
                if (prefs.getInteger("WIN_COUNT", 0) < 20) {
                    prefs.putInteger("WIN_COUNT", 20);
                }
                prefs.putBoolean(_p.chosenClass.name() + "_WIN", true);
                prefs.putBoolean("ASCEND_0", true);
                prefs.putInteger("ASCENSION_LEVEL", 20);
                prefs.flush();
            }
        }
    }
    @Override
    public void receiveEditKeywords() {
        // 暂只支持中文
        String lang = "zhs";
        Gson gson = new Gson();
        String json = Gdx.files.internal("localization/" + lang + "/KeywordStrings.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        // 开战时再清一次，兜底战斗结束清理晚于 onRemove 压栈的情况
        PowerHistory.clear();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        // 战斗结束清空 Power 的历史记录，避免跨战斗残留
        PowerHistory.clear();
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) return;

        int HAmount = TheLastKnightModHelper.GetWillH(p);
        if (HAmount > 0){
            p.heal(HAmount);
        } else if (HAmount < 0) {
            // GetWillH 已处理狂徒妄念的免死上限；DamageInfo 会把负数钳制为 0，这里取正值
            p.damage(new DamageInfo(
                    null,
                    -HAmount,
                    DamageInfo.DamageType.HP_LOSS
            ));
        }
    }

}
