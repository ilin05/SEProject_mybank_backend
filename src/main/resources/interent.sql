/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : mybank

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 13/06/2024 13:24:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cashier
-- ----------------------------
DROP TABLE IF EXISTS `cashier`;
CREATE TABLE `cashier`  (
  `cashier_id` int(0) NOT NULL AUTO_INCREMENT,
  `cashier_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `privilege` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`cashier_id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cashier
-- ----------------------------

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `customer_id` int(0) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `credit_line` decimal(20, 2) NULL DEFAULT 0.00,
  `assets` decimal(20, 2) NULL DEFAULT 0.00,
  `internet_bank_open` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`customer_id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for fixed_deposit
-- ----------------------------
DROP TABLE IF EXISTS `fixed_deposit`;
CREATE TABLE `fixed_deposit`  (
  `fixed_deposit_id` int(0) NOT NULL AUTO_INCREMENT,
  `account_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `deposit_time` datetime(0) NOT NULL,
  `deposit_amount` decimal(20, 2) NOT NULL,
  `deposit_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_renewal` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`fixed_deposit_id`) USING BTREE,
  INDEX `account_id`(`account_id`) USING BTREE,
  INDEX `deposit_type`(`deposit_type`) USING BTREE,
  CONSTRAINT `fixed_deposit_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `saving_account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fixed_deposit_ibfk_2` FOREIGN KEY (`deposit_type`) REFERENCES `fixed_deposit_type` (`deposit_type`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fixed_deposit
-- ----------------------------

-- ----------------------------
-- Table structure for fixed_deposit_type
-- ----------------------------
DROP TABLE IF EXISTS `fixed_deposit_type`;
CREATE TABLE `fixed_deposit_type`  (
  `deposit_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deposit_duration` int(0) NOT NULL,
  `deposit_rate` decimal(10, 6) NOT NULL,
  PRIMARY KEY (`deposit_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fixed_deposit_type
-- ----------------------------
INSERT INTO `fixed_deposit_type` VALUES ('A', 3, 0.011500);
INSERT INTO `fixed_deposit_type` VALUES ('B', 6, 0.013500);
INSERT INTO `fixed_deposit_type` VALUES ('C', 12, 0.014500);
INSERT INTO `fixed_deposit_type` VALUES ('D', 24, 0.016500);
INSERT INTO `fixed_deposit_type` VALUES ('E', 36, 0.019500);
INSERT INTO `fixed_deposit_type` VALUES ('F', 60, 0.020000);

-- ----------------------------
-- Table structure for freeze_state_record
-- ----------------------------
DROP TABLE IF EXISTS `freeze_state_record`;
CREATE TABLE `freeze_state_record`  (
  `freeze_state_record_id` int(0) NOT NULL AUTO_INCREMENT,
  `account_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `freeze_time` datetime(0) NOT NULL,
  `unfreeze_time` datetime(0) NOT NULL,
  `freeze_reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`freeze_state_record_id`) USING BTREE,
  INDEX `account_id`(`account_id`) USING BTREE,
  CONSTRAINT `freeze_state_record_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `saving_account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freeze_state_record
-- ----------------------------

-- ----------------------------
-- Table structure for interent
-- ----------------------------
DROP TABLE IF EXISTS `internet`;
CREATE TABLE `internet`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `customer_account_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `customer_id` int(0) NULL DEFAULT NULL,
  `is_in_black_list` tinyint(1) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interent
-- ----------------------------

-- ----------------------------
-- Table structure for loss_state_record
-- ----------------------------
DROP TABLE IF EXISTS `loss_state_record`;
CREATE TABLE `loss_state_record`  (
  `loss_state_record_id` int(0) NOT NULL AUTO_INCREMENT,
  `account_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `loss_time` datetime(0) NOT NULL,
  `reissue_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`loss_state_record_id`) USING BTREE,
  INDEX `account_id`(`account_id`) USING BTREE,
  CONSTRAINT `loss_state_record_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `saving_account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of loss_state_record
-- ----------------------------

-- ----------------------------
-- Table structure for saving_account
-- ----------------------------
DROP TABLE IF EXISTS `saving_account`;
CREATE TABLE `saving_account`  (
  `account_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `customer_id` int(0) NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `balance` decimal(20, 2) NOT NULL,
  `freeze_state` tinyint(1) NULL DEFAULT 0,
  `loss_state` tinyint(1) NULL DEFAULT 0,
  `deleted` tinyint(1) NULL DEFAULT 0,
  `open_time` datetime(0) NOT NULL,
  `open_amount` decimal(20, 2) NOT NULL,
  `interest` decimal(20, 6) NULL DEFAULT 0.000000,
  PRIMARY KEY (`account_id`) USING BTREE,
  INDEX `customer_id`(`customer_id`) USING BTREE,
  CONSTRAINT `saving_account_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of saving_account
-- ----------------------------

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`  (
  `transaction_id` int(0) NOT NULL AUTO_INCREMENT,
  `card_id` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `card_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `transaction_time` datetime(0) NOT NULL,
  `transaction_amount` decimal(20, 2) NOT NULL,
  `transaction_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `transaction_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `currency` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'CNY',
  `money_source` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `money_goes` char(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`transaction_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transaction
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
