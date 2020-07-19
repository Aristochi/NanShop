
package com.NanShop.mall.common;

public enum NanShopMallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    NanShopMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static NanShopMallCategoryLevelEnum getNanShopMallOrderStatusEnumByLevel(int level) {
        for (NanShopMallCategoryLevelEnum nanShopMallCategoryLevelEnum : NanShopMallCategoryLevelEnum.values()) {
            if (nanShopMallCategoryLevelEnum.getLevel() == level) {
                return nanShopMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
