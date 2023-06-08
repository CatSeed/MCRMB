package com.mcrmb;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类提供了一些与充值卡服务有关的实用方法。
 */
public class CardService {
    // 私有化构造方法
    private CardService() {
        initCardStatusMap();
    }

    // 存储卡状态码对应的描述
    private final Map<String, String> cardStatusMap = new HashMap<>();

    // 单例对象
    private static final CardService instance = new CardService();

    /**
     * 初始化卡状态码与描述的映射关系
     */
    private void initCardStatusMap() {
        cardStatusMap.put("0", "卡异常，联系服主！");
        cardStatusMap.put("100", "卡异常，联系服主！");
        cardStatusMap.put("101", "卡异常，联系服主！");
        cardStatusMap.put("102", "卡异常，联系服主！");
        cardStatusMap.put("103", "卡异常，联系服主！");
        cardStatusMap.put("104", "卡异常，联系服主！");
        cardStatusMap.put("105", "卡异常，联系服主！");
        cardStatusMap.put("106", "卡异常，联系服主！");
        cardStatusMap.put("200", "卡密处理中！");
        cardStatusMap.put("201", "卡密不正确！");
        cardStatusMap.put("202", "不支持这种卡！");
        cardStatusMap.put("203", "处理异常，联系服主！");
        cardStatusMap.put("204", "不支持这种面值的卡！");
        cardStatusMap.put("205", "处理异常，联系服主！");
        cardStatusMap.put("206", "运营商维护中！");
        cardStatusMap.put("208", "卡已经被使用过了！");
        cardStatusMap.put("209", "卡提交过于频繁！");
        cardStatusMap.put("210", "处理异常，联系服主！");
        cardStatusMap.put("300", "卡已经处理成功！");
        cardStatusMap.put("302", "卡密处理失败！");
        cardStatusMap.put("303", "卡号无效！");
        cardStatusMap.put("304", "卡密错误！");
        cardStatusMap.put("305", "卡上余额不足！");
        cardStatusMap.put("306", "卡的状态错误！");
    }

    /**
     * 获取单例对象
     */
    public static CardService getInstance() {
        return instance;
    }

    /**
     * 根据卡号获取卡状态
     *
     * @param cardNumber 卡号
     * @return 卡状态，如果卡号不存在则返回“卡密有误，请检查！”
     */
    public String getCardStatus(String cardNumber) {
        try {
            return cardStatusMap.get(cardNumber);
        } catch (Exception e) {
            return "卡密有误,请检查!";
        }
    }
}

