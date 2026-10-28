package cards;

import basemod.abstracts.CustomCard;

/**
 * 不实现任何功能，仅用于标识属于美德卡牌，便于后续通过 instanceof 统一判断。
 */
public abstract class Virtue extends CustomCard {
    protected Virtue(String id, String name, String img, int cost, String rawDescription,
                     CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }
}
