-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: utescihub
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_acceptance_requests`
--

DROP TABLE IF EXISTS `tbl_acceptance_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_acceptance_requests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `acknowledgment` bit(1) NOT NULL,
  `attempt_number` int NOT NULL,
  `notes` text,
  `status` tinyint NOT NULL,
  `submission_date` date NOT NULL,
  `topic_id` varchar(255) NOT NULL,
  `is_final` bit(1) NOT NULL,
  `council_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvi829q9ndcnfj4ogmhecqgv3` (`topic_id`),
  KEY `FKbq0qw35k7b77dw6780blvmgvi` (`council_id`),
  CONSTRAINT `FKbq0qw35k7b77dw6780blvmgvi` FOREIGN KEY (`council_id`) REFERENCES `tbl_councils` (`id`),
  CONSTRAINT `FKrvi829q9ndcnfj4ogmhecqgv3` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_acceptance_requests`
--

LOCK TABLES `tbl_acceptance_requests` WRITE;
/*!40000 ALTER TABLE `tbl_acceptance_requests` DISABLE KEYS */;
INSERT INTO `tbl_acceptance_requests` VALUES (1,'2025-05-31 23:00:32.300408','21115053120126','2025-05-31 23:00:41.186648','21115053120126',_binary '',1,'Ghi chú bổ sung',0,'2025-05-31','469737e5-162d-4a21-8a5b-202d87f91712',_binary '\0',NULL),(2,'2025-05-31 23:03:25.118535','21115053120126','2025-06-01 18:12:03.809493','211115053120159',_binary '',2,'Ghi chú bổ sung',1,'2025-05-31','469737e5-162d-4a21-8a5b-202d87f91712',_binary '',NULL);
/*!40000 ALTER TABLE `tbl_acceptance_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_categories`
--

DROP TABLE IF EXISTS `tbl_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_categories`
--

LOCK TABLES `tbl_categories` WRITE;
/*!40000 ALTER TABLE `tbl_categories` DISABLE KEYS */;
INSERT INTO `tbl_categories` VALUES (1,'2025-04-27 17:57:39.980134','211115053120159','2025-05-01 22:34:50.848043','211115053120159',_binary '\0','Đề tài cấp khoa',NULL,'Loại A'),(2,'2025-04-27 19:51:43.227668','211115053120159','2025-05-01 22:34:50.848043','211115053120159',_binary '\0','Đề tài loại B',NULL,'Loại B'),(3,'2025-04-28 23:13:03.540633','211115053120159','2025-05-01 22:34:50.848043','211115053120159',_binary '\0','Đề tài loại',NULL,'Loại C'),(4,'2025-04-28 23:13:48.565172','211115053120159','2025-05-01 22:34:50.850048','211115053120159',_binary '\0','không chỉ nghiên cứu lý thuyết như các đề tài nghiên cứu cơ bản, mà còn có yếu tố triển khai thực tiễn rõ ràng: ví dụ như chế tạo sản phẩm mẫu, xây dựng hệ thống phần mềm, thử nghiệm quy trình sản xuất mới,...',NULL,'Đề án khoa học và công nghệ'),(5,'2025-05-03 13:20:49.340269','211115053120159','2025-05-03 13:20:49.340269','211115053120159',_binary '\0','test',NULL,'test');
/*!40000 ALTER TABLE `tbl_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_contracts`
--

DROP TABLE IF EXISTS `tbl_contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_contracts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `contract_details` text,
  `contract_path` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `signed_date` date DEFAULT NULL,
  `topic_id` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `status` enum('CANCELLED','PENDING','SIGNED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKptxf9lhvsvy41lfe308amj3dx` (`topic_id`),
  CONSTRAINT `FKptxf9lhvsvy41lfe308amj3dx` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_contracts`
--

LOCK TABLES `tbl_contracts` WRITE;
/*!40000 ALTER TABLE `tbl_contracts` DISABLE KEYS */;
INSERT INTO `tbl_contracts` VALUES (1,'2025-05-25 21:20:34.621323','211115053120159','2025-05-25 22:13:07.417203','211115053120159','Mô tả chi tiết về nội dung hợp đồng, các điều khoản quan trọng và nghĩa vụ của các bên.','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748182833/UTE-SciHub/pdf/swjgs9mu1kg2tp46crwd.pdf',_binary '\0','2025-05-25','0810b394-11f1-4384-8ec7-1ad6f181660c','UTE-HD-00111','Hợp đồng thực hiện đề tài 003','SIGNED');
/*!40000 ALTER TABLE `tbl_contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_council_members`
--

DROP TABLE IF EXISTS `tbl_council_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_council_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `role` enum('CHAIRMAN','REVIEWER','MEMBER','SECRETARY') DEFAULT NULL,
  `council_id` bigint DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1vxqrp2qtjso05px7gsrfy4fo` (`council_id`),
  KEY `FK1f1prgqe6wvnfx7fxp2cg35hk` (`user_id`),
  CONSTRAINT `FK1f1prgqe6wvnfx7fxp2cg35hk` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`),
  CONSTRAINT `FK1vxqrp2qtjso05px7gsrfy4fo` FOREIGN KEY (`council_id`) REFERENCES `tbl_councils` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_council_members`
--

LOCK TABLES `tbl_council_members` WRITE;
/*!40000 ALTER TABLE `tbl_council_members` DISABLE KEYS */;
INSERT INTO `tbl_council_members` VALUES (1,'2025-05-06 23:11:34.771905','211115053120159','2025-05-06 23:11:34.771905','211115053120159','CHAIRMAN',1,'1001'),(2,'2025-05-06 23:11:34.792524','211115053120159','2025-05-06 23:11:34.792524','211115053120159','MEMBER',1,'1002'),(3,'2025-05-07 23:02:20.478149','211115053120159','2025-05-08 21:32:05.489103','211115053120159','MEMBER',2,'211115053120134'),(4,'2025-05-07 23:02:20.484124','211115053120159','2025-05-08 21:32:05.501620','211115053120159','CHAIRMAN',2,'doe'),(6,'2025-05-24 16:24:05.874545','211115053120159','2025-05-24 16:24:05.874545','211115053120159','CHAIRMAN',3,'doe'),(7,'2025-05-24 16:24:05.880211','211115053120159','2025-05-24 16:24:05.880211','211115053120159','MEMBER',3,'211115053120159'),(8,'2025-05-24 16:28:06.524877','211115053120159','2025-05-24 16:28:06.524877','211115053120159','MEMBER',3,'21115053120122'),(9,'2025-06-01 15:01:20.798359','211115053120159','2025-06-01 15:01:20.798359','211115053120159','MEMBER',4,'5'),(10,'2025-06-01 15:01:20.803117','211115053120159','2025-06-01 15:01:20.803117','211115053120159','CHAIRMAN',4,'211115053120100'),(11,'2025-06-01 15:01:20.805200','211115053120159','2025-06-01 15:01:20.805200','211115053120159','MEMBER',4,'17'),(12,'2025-06-14 21:14:31.946337','211115053120159','2025-06-14 21:14:31.946337','211115053120159','CHAIRMAN',5,'doe'),(13,'2025-06-14 21:14:31.952905','211115053120159','2025-06-14 21:14:31.952905','211115053120159','MEMBER',5,'211115053120134'),(14,'2025-06-14 21:14:31.957430','211115053120159','2025-06-14 21:14:31.957430','211115053120159','MEMBER',5,'19'),(15,'2025-06-14 21:14:31.959434','211115053120159','2025-06-14 21:14:31.959434','211115053120159','MEMBER',5,'2');
/*!40000 ALTER TABLE `tbl_council_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_councils`
--

DROP TABLE IF EXISTS `tbl_councils`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_councils` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `decision_number` varchar(50) NOT NULL,
  `end_date` date NOT NULL,
  `establishment_date` date NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `start_date` date NOT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `type` enum('EVALUATE_TOPIC','SELECT_CNDT','ACCEPTANCE_JURY') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_councils`
--

LOCK TABLES `tbl_councils` WRITE;
/*!40000 ALTER TABLE `tbl_councils` DISABLE KEYS */;
INSERT INTO `tbl_councils` VALUES (1,'2025-05-06 23:11:34.464799','211115053120159','2025-05-08 22:08:59.110812','211115053120159','QĐ-2025-04-02','2025-05-09','2025-05-05','test','ok','2025-05-06',_binary '','SELECT_CNDT'),(2,'2025-05-07 23:02:20.437237','211115053120159','2025-05-09 22:44:58.031255','211115053120159','QD-2025-1235','2025-05-22','2025-05-06','Hội đồng xét duyệt CNDT đợt 2 năm 2025','','2025-05-07',_binary '','SELECT_CNDT'),(3,'2025-05-24 16:24:05.829052','211115053120159','2025-05-24 16:24:05.829052','211115053120159','QĐ-2025-04-023','2025-06-29','2025-05-23','Hội đồng xét duyệt đề tài đợt 3 năm 2025','','2025-05-25',_binary '\0','SELECT_CNDT'),(4,'2025-06-01 15:01:20.732774','211115053120159','2025-06-01 15:01:20.732774','211115053120159','QD-20250408-009','2025-06-19','2025-05-31','Hội đồng nghiệm thu lần 1 2025','','2025-06-01',_binary '\0','ACCEPTANCE_JURY'),(5,'2025-06-14 21:14:31.867944','211115053120159','2025-06-14 21:14:31.867944','211115053120159','QĐ-2025-06-003','2025-06-26','2025-06-14','Hội đồng xét duyệt CNDT đợt 3 năm 2025','','2025-06-15',_binary '\0','SELECT_CNDT');
/*!40000 ALTER TABLE `tbl_councils` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_departments`
--

DROP TABLE IF EXISTS `tbl_departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_departments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `logo_public_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_departments`
--

LOCK TABLES `tbl_departments` WRITE;
/*!40000 ALTER TABLE `tbl_departments` DISABLE KEYS */;
INSERT INTO `tbl_departments` VALUES (1,'2025-03-29 10:26:29.846212','211115053120159','2025-03-29 16:01:58.811596','211115053120159',_binary '\0','Quản lý các ngành CNTT','fdt@ute.udn.vn','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpO_v7PNEZKG2YzeyNLhIHGWBkDdcRp9ZadQ&s','Khoa Công nghệ số','0123456789',NULL),(2,'2025-03-29 10:27:50.130985','211115053120159','2025-06-08 13:42:44.082704','211115053120159',_binary '\0','Quản lý các ngành khoa điện','ddt@ute.udn.vn','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744731988/UTE-SciHub/images/spgwyod08dvub2chwsno.jpg','Khoa Điện - Điện tử','0123456789','UTE-SciHub/images/spgwyod08dvub2chwsno'),(3,'2025-03-29 15:21:32.136169','211115053120159','2025-05-26 22:00:30.569625','211115053120159',_binary '\0','','chemistry@ute.ud.vn','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744732026/UTE-SciHub/images/vawydd4jpdrw4tpvsbgw.jpg','Khoa Công nghệ hóa học - Môi trường','0912345678','UTE-SciHub/images/vawydd4jpdrw4tpvsbgw'),(4,'2025-04-15 22:57:31.140724','211115053120159','2025-04-15 22:59:55.219438','211115053120159',_binary '\0','','khoaktxd.ute@gmail.com','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744732795/UTE-SciHub/images/dkfigriminaplnfpanqz.jpg','Khoa Kỹ thuật Xây dựng','02363519690','UTE-SciHub/images/dkfigriminaplnfpanqz'),(5,'2025-04-15 23:01:11.112288','211115053120159','2025-04-15 23:01:11.113315','211115053120159',_binary '\0','','nlcthanh@ute.udn.vn','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744732871/UTE-SciHub/images/dabthokhbvvem871itbb.png','Khoa Cơ khí','0989296540','UTE-SciHub/images/dabthokhbvvem871itbb'),(6,'2025-04-16 22:34:57.019094','211115053120158','2025-04-23 23:22:20.254986','211115053120159',_binary '\0','','k.spcn@ute.udn.vn','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744817696/UTE-SciHub/images/gpfzfqj8yutpo7nn0sbz.jpg','Khoa Sư phạm Công nghiệp','0989296549','UTE-SciHub/images/gpfzfqj8yutpo7nn0sbz');
/*!40000 ALTER TABLE `tbl_departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_documents`
--

DROP TABLE IF EXISTS `tbl_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_documents` (
  `id` int NOT NULL AUTO_INCREMENT,
  `document_type` varchar(50) DEFAULT NULL,
  `file_path` text,
  `upload_date` datetime(6) DEFAULT NULL,
  `topic_id` varchar(255) NOT NULL,
  `original_file_name` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `acceptance_request_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKolt13k5pyqki018rmt4o4w5e` (`topic_id`),
  KEY `FKtefqmwiaedxxijb6rx88n77xd` (`acceptance_request_id`),
  CONSTRAINT `FKolt13k5pyqki018rmt4o4w5e` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`),
  CONSTRAINT `FKtefqmwiaedxxijb6rx88n77xd` FOREIGN KEY (`acceptance_request_id`) REFERENCES `tbl_acceptance_requests` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_documents`
--

LOCK TABLES `tbl_documents` WRITE;
/*!40000 ALTER TABLE `tbl_documents` DISABLE KEYS */;
INSERT INTO `tbl_documents` VALUES (1,'Biên bản đánh giá','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1746262354/UTE-SciHub/pdf/xnlrpxdq3kdes1qfm1vi.pdf','2025-05-03 15:52:34.863107','0810b394-11f1-4384-8ec7-1ad6f181660c','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/xnlrpxdq3kdes1qfm1vi.pdf',NULL,NULL,NULL,NULL,0,NULL),(2,'Phiếu đăng ký đề tài','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1746881914/UTE-SciHub/pdf/geefqukyopvamdduqedp.pdf','2025-05-10 19:58:35.501055','85882a05-f291-4d76-a079-76abeaa3a723','Võ Trung Hùng - 21115053120158 - Lê Thanh Tuấn - ERD.pdf','UTE-SciHub/pdf/geefqukyopvamdduqedp.pdf',NULL,NULL,NULL,NULL,0,NULL),(3,'Phiếu đăng ký đề tài','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1747471753/UTE-SciHub/pdf/hhpo3uqlivi0pdev2obi.pdf','2025-05-17 15:49:13.778269','40078327-5bc5-4960-877a-c81b9647417d','Bản sao của Phần Lịch.pdf','UTE-SciHub/pdf/hhpo3uqlivi0pdev2obi.pdf','2025-05-17 15:49:13.843937','211115053120100','2025-05-17 15:49:13.843937','211115053120100',0,NULL),(4,'Phiếu đăng ký đề tài','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748075524/UTE-SciHub/pdf/stwlmuczzchwu4iiayon.pdf','2025-05-24 15:32:04.622287','dfd7f292-6a41-491b-bd6c-bee1c691fb87','QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường (1) (1).pdf','UTE-SciHub/pdf/stwlmuczzchwu4iiayon.pdf','2025-05-24 15:32:04.672419','21115053120125','2025-05-24 15:32:04.672419','21115053120125',0,NULL),(5,'Biên bản đánh giá','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748097305/UTE-SciHub/pdf/lsvw2avqohhvgtvsyoj2.pdf','2025-05-24 21:35:05.597236','40078327-5bc5-4960-877a-c81b9647417d','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/lsvw2avqohhvgtvsyoj2.pdf','2025-05-24 21:35:05.807638','21115053120122','2025-05-24 21:35:05.807638','21115053120122',0,NULL),(6,'Báo cáo tổng kết','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748707238/UTE-SciHub/pdf/geccaj662bjon2qen6cx.pdf','2025-05-31 23:00:41.036363','469737e5-162d-4a21-8a5b-202d87f91712','QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường (1) (1).pdf','UTE-SciHub/pdf/geccaj662bjon2qen6cx.pdf','2025-05-31 23:00:41.050032','21115053120126','2025-05-31 23:00:41.050032','21115053120126',1,'QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường'),(7,'Báo cáo tổng kết','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748707240/UTE-SciHub/pdf/mgrkged255mvlvzafkxb.pdf','2025-05-31 23:00:41.036978','469737e5-162d-4a21-8a5b-202d87f91712','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/mgrkged255mvlvzafkxb.pdf','2025-05-31 23:00:41.060619','21115053120126','2025-05-31 23:00:41.060619','21115053120126',1,'BM.05-QT.01-KHCN'),(8,'Báo cáo tổng kết','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748707409/UTE-SciHub/pdf/ixhuthnw7cl1iitnfjx3.pdf','2025-05-31 23:03:32.359543','469737e5-162d-4a21-8a5b-202d87f91712','QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường (1) (1).pdf','UTE-SciHub/pdf/ixhuthnw7cl1iitnfjx3.pdf','2025-05-31 23:03:32.364237','21115053120126','2025-05-31 23:03:32.364237','21115053120126',2,'QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường'),(9,'Quyết định BM.24-QT.01-KHCN','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748707411/UTE-SciHub/pdf/cmr4j4pf1d2rb33dbf1h.pdf','2025-05-31 23:03:32.359543','469737e5-162d-4a21-8a5b-202d87f91712','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/cmr4j4pf1d2rb33dbf1h.pdf','2025-05-31 23:03:32.369304','21115053120126','2025-05-31 23:03:32.369304','21115053120126',2,'BM.05-QT.01-KHCN'),(10,'Quyết định','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748776321/UTE-SciHub/pdf/euzdacr70nhyofykitwy.pdf','2025-06-01 18:12:01.910854','469737e5-162d-4a21-8a5b-202d87f91712','shopee.pdf','UTE-SciHub/pdf/euzdacr70nhyofykitwy.pdf','2025-06-01 18:12:02.115749','211115053120159','2025-06-01 18:12:02.115749','211115053120159',2,'Quyết định nghiệm thu'),(11,'Quyết định','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748776323/UTE-SciHub/pdf/toh0pvg0ga2gfi35voks.pdf','2025-06-01 18:12:03.755367','469737e5-162d-4a21-8a5b-202d87f91712','shopee.pdf','UTE-SciHub/pdf/toh0pvg0ga2gfi35voks.pdf','2025-06-01 18:12:03.767179','211115053120159','2025-06-01 18:12:03.767179','211115053120159',2,'Quyết định nghiệm thu'),(12,'Biên bản đánh giá','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1749739644/UTE-SciHub/pdf/lgkxd6epn9bu1y3wqslw.pdf','2025-06-12 21:47:24.435442','469737e5-162d-4a21-8a5b-202d87f91712','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/lgkxd6epn9bu1y3wqslw.pdf','2025-06-12 21:47:24.583520','211115053120100','2025-06-12 21:47:24.584520','211115053120100',NULL,NULL),(13,'Phiếu đăng ký đề tài','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1749907964/UTE-SciHub/pdf/rxu5ldncu8kanid3do3d.pdf','2025-06-14 20:32:45.073389','e9618cf7-f89f-45cc-890a-0bb07c837c5a','erd2.pdf','UTE-SciHub/pdf/rxu5ldncu8kanid3do3d.pdf','2025-06-14 20:32:45.204523','12','2025-06-14 20:32:45.204523','12',NULL,NULL),(14,'Biên bản đánh giá','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1749913023/UTE-SciHub/pdf/h0ogmla9xjyksnu4m4jy.pdf','2025-06-14 21:57:03.863029','e9618cf7-f89f-45cc-890a-0bb07c837c5a','BM.05-QT.01-KHCN.pdf','UTE-SciHub/pdf/h0ogmla9xjyksnu4m4jy.pdf','2025-06-14 21:57:03.920944','doe','2025-06-14 21:57:03.920944','doe',NULL,NULL);
/*!40000 ALTER TABLE `tbl_documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_evaluation_details`
--

DROP TABLE IF EXISTS `tbl_evaluation_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_evaluation_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `approach_method_score` int DEFAULT NULL,
  `budget_score` int DEFAULT NULL,
  `content_and_timeline_score` int DEFAULT NULL,
  `effectiveness_score` int DEFAULT NULL,
  `experience_score` int DEFAULT NULL,
  `institution_capability_score` int DEFAULT NULL,
  `objective_score` int DEFAULT NULL,
  `product_score` int DEFAULT NULL,
  `research_overview_score` int DEFAULT NULL,
  `urgency_score` int DEFAULT NULL,
  `council_member_id` bigint DEFAULT NULL,
  `evaluation_id` bigint DEFAULT NULL,
  `additional_comments` text,
  PRIMARY KEY (`id`),
  KEY `FKpujolcjp1twduerdqbdw3vkuy` (`council_member_id`),
  KEY `FKih86m45m7d99ks917lscbtojr` (`evaluation_id`),
  CONSTRAINT `FKih86m45m7d99ks917lscbtojr` FOREIGN KEY (`evaluation_id`) REFERENCES `tbl_topic_applications` (`id`),
  CONSTRAINT `FKpujolcjp1twduerdqbdw3vkuy` FOREIGN KEY (`council_member_id`) REFERENCES `tbl_council_members` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_evaluation_details`
--

LOCK TABLES `tbl_evaluation_details` WRITE;
/*!40000 ALTER TABLE `tbl_evaluation_details` DISABLE KEYS */;
INSERT INTO `tbl_evaluation_details` VALUES (1,'2025-05-09 22:22:32.027319','1001','2025-05-09 22:22:32.027319','1001',3,3,10,5,3,3,7,12,3,6,1,5,''),(2,'2025-05-10 12:52:29.027357','211115053120134','2025-05-10 12:52:29.027357','211115053120134',3,3,10,5,3,3,7,12,3,6,3,5,''),(3,'2025-05-10 13:43:46.009614','1002','2025-05-10 13:43:46.009614','1002',3,3,10,5,3,3,7,12,3,6,2,6,''),(4,'2025-05-17 22:36:22.339497','211115053120134','2025-05-17 22:36:22.339497','211115053120134',3,3,10,5,3,3,7,12,3,6,3,3,''),(5,'2025-05-27 20:21:03.179074','21115053120122','2025-05-27 20:21:03.179074','21115053120122',3,7,10,9,3,3,7,12,3,6,8,7,''),(6,'2025-05-27 22:09:08.977419','211115053120159','2025-05-27 22:09:08.977419','211115053120159',3,3,10,9,3,3,7,12,3,6,7,8,''),(7,'2025-05-27 22:41:47.712285','211115053120159','2025-05-27 22:41:47.712285','211115053120159',3,3,10,9,3,3,7,17,3,6,7,7,''),(8,'2025-06-14 22:06:28.882163','doe','2025-06-14 22:06:28.882163','doe',3,3,10,5,3,3,10,12,3,8,12,9,''),(9,'2025-06-14 22:06:38.294948','doe','2025-06-14 22:06:38.294948','doe',3,3,10,5,3,3,7,12,3,6,12,10,''),(10,'2025-06-14 22:22:52.400928','19','2025-06-14 22:22:52.400928','19',3,3,10,5,3,3,7,12,3,6,14,9,''),(11,'2025-06-14 22:23:01.637693','19','2025-06-14 22:23:01.637693','19',3,3,10,5,3,3,7,12,3,6,14,10,'');
/*!40000 ALTER TABLE `tbl_evaluation_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_milestones`
--

DROP TABLE IF EXISTS `tbl_milestones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_milestones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `description` varchar(225) DEFAULT NULL,
  `expected_completion_date` date DEFAULT NULL,
  `status` enum('COMPLETED','IN_PROGRESS','PENDING') DEFAULT NULL,
  `topic_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe7dffw8rtlnunn0hia6sfakrj` (`topic_id`),
  CONSTRAINT `FKe7dffw8rtlnunn0hia6sfakrj` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_milestones`
--

LOCK TABLES `tbl_milestones` WRITE;
/*!40000 ALTER TABLE `tbl_milestones` DISABLE KEYS */;
INSERT INTO `tbl_milestones` VALUES (2,'2025-05-17 17:31:26.950247','211115053120159','2025-05-17 21:16:36.789040','211115053120159',_binary '\0','Giai đoạn 1','2025-05-19','IN_PROGRESS','b8751fb5-2ecd-472e-a7be-462e7d372096'),(3,'2025-05-17 17:31:50.249077','211115053120159','2025-05-17 18:40:51.332554','211115053120159',_binary '\0','Giai đoạn 2','2025-05-30','IN_PROGRESS','b8751fb5-2ecd-472e-a7be-462e7d372096'),(4,'2025-05-17 17:56:24.749056','211115053120159','2025-05-17 17:56:24.749056','211115053120159',_binary '\0','Giai đoạn 3','2025-06-11','COMPLETED','b8751fb5-2ecd-472e-a7be-462e7d372096'),(5,'2025-05-30 21:13:47.654697','21115053120126','2025-06-10 22:28:03.682396','21115053120126',_binary '\0','Giai đoạn 1','2025-06-19','COMPLETED','40078327-5bc5-4960-877a-c81b9647417d'),(6,'2025-06-10 22:27:11.232632','21115053120126','2025-06-10 22:27:11.232632','21115053120126',_binary '\0','Đây là giai đoạn chuẩn bị tài liệu','2025-06-19','IN_PROGRESS','40078327-5bc5-4960-877a-c81b9647417d');
/*!40000 ALTER TABLE `tbl_milestones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_progresses`
--

DROP TABLE IF EXISTS `tbl_progresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_progresses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `document_url` varchar(255) DEFAULT NULL,
  `progress_percent` int DEFAULT NULL,
  `report` text,
  `milestone_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6onde5o83g6fgfdef9ii4n8uj` (`milestone_id`),
  CONSTRAINT `FK6onde5o83g6fgfdef9ii4n8uj` FOREIGN KEY (`milestone_id`) REFERENCES `tbl_milestones` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_progresses`
--

LOCK TABLES `tbl_progresses` WRITE;
/*!40000 ALTER TABLE `tbl_progresses` DISABLE KEYS */;
INSERT INTO `tbl_progresses` VALUES (4,'2025-05-17 21:16:53.052709','211115053120159','2025-05-17 23:04:28.426570','211115053120159',NULL,100,'Báo cáo tiến độ Báo cáo tiến độ Báo cáo tiến độ',2),(5,'2025-05-17 21:55:21.529191','211115053120159','2025-05-17 21:55:21.529191','211115053120159',NULL,100,'ok hoàn thành r nè',4),(7,'2025-05-18 22:48:21.967755','211115053120159','2025-05-18 23:07:13.644725','211115053120159','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1747583301/UTE-SciHub/pdf/i3qluvxf0m6tkb1p6aut.pdf',50,'Thêm file báo cáo chi tiết',3),(8,'2025-05-18 23:02:36.604360','211115053120159','2025-05-18 23:02:36.604360','211115053120159','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1747584156/UTE-SciHub/pdf/xv4lk6mqrnfoidikmszb.pdf',100,'báo cáo tiến độ Giai đoạn 2',3),(9,'2025-05-18 23:06:49.442592','211115053120159','2025-05-18 23:06:49.442592','211115053120159','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1747584409/UTE-SciHub/pdf/wxqkntw7g1pjdsguurmf.pdf',100,'Thêm báo cáo tiến độ Giai đoạn 3',4),(10,'2025-05-20 22:33:57.454402','211115053120159','2025-05-24 15:18:02.343712','211115053120159',NULL,75,'gfujfhjghfhjfdhgdfghf',2),(11,'2025-06-10 22:27:43.821442','21115053120126','2025-06-10 22:27:43.821442','21115053120126',NULL,100,'test báo cáo',5),(12,'2025-06-12 20:40:48.789664','21115053120126','2025-06-12 20:40:48.789664','21115053120126',NULL,50,'ávadfafsdf',6);
/*!40000 ALTER TABLE `tbl_progresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_registration_periods`
--

DROP TABLE IF EXISTS `tbl_registration_periods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_registration_periods` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `decision_file` text,
  `decision_number` varchar(100) DEFAULT NULL,
  `description` text,
  `end_date` date NOT NULL,
  `image_url` text,
  `start_date` date NOT NULL,
  `status` enum('CANCELLED','CLOSED','OPEN','REVIEWING') NOT NULL,
  `title` varchar(255) NOT NULL,
  `file_public_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_registration_periods`
--

LOCK TABLES `tbl_registration_periods` WRITE;
/*!40000 ALTER TABLE `tbl_registration_periods` DISABLE KEYS */;
INSERT INTO `tbl_registration_periods` VALUES ('001_UTE_20250319_DK','2025-03-19 20:46:29.772880','unknown','2025-04-13 16:19:15.836030','211115053120159','http://localhost:8080/api/v1/files/https://example.com/files/decision_2025.pdf','QĐ-2025-01','<p>Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.</p>','2025-06-30',NULL,'2025-04-01','OPEN','Đợt đăng ký nghiên cứu 2025',NULL),('002_UTE_20250320_DK','2025-03-20 08:36:49.068726','unknown','2025-03-20 08:36:49.068726','unknown','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('003_UTE_20250320_DK','2025-03-20 08:43:22.775484','unknown','2025-03-20 08:43:22.775484','unknown','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('005_UTE_20250320_DK','2025-03-20 08:48:12.608987','unknown','2025-03-20 08:48:12.608987','unknown','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('006_UTE_20250320_DK','2025-03-20 08:49:17.319724','unknown','2025-03-20 08:49:17.319724','unknown','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('007_UTE_20250320_DK','2025-03-20 08:50:31.790166','unknown','2025-04-07 20:11:51.861769','211115053120159','DeThiMySQL.pdf','QĐ-2025-0123','<p>Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.</p>','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('008_UTE_20250320_DK','2025-03-20 08:56:31.761272','unknown','2025-03-20 08:56:31.761272','unknown','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('009_UTE_20250320_DK','2025-03-20 09:01:35.456946','211115053120159','2025-03-20 09:01:35.456946','211115053120159','https://example.com/files/decision_2025.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('010_UTE_20250320_DK','2025-03-20 09:02:32.454587','211115053120159','2025-04-20 11:35:53.309489','211115053120159','http://localhost:8080/api/v1/files/https://example.com/files/decision_2025.pdf','QĐ-2025-0123','<p style=\"text-align: center;\"><span style=\"text-decoration: underline;\"><em><strong>Đợt đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học cấp trường năm 2025.</strong></em></span></p>\n<p style=\"text-align: center;\"><span style=\"text-decoration: underline;\"><em><strong><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABekAAAH6CAIAAAAcN1B3AAAgAElEQVR4Aezda7QcZYEvfCbnC+us98NZ84W15sO41pZxji/O2eBlPN62ERQvZEZGhZEoMwrIwBAxeFfwxoAoAxpFDBcJ4ZJAlAQSEgREgoKEkHALIVyCkSSGhIu5QEJ2kr13v291dVVXVV+qu/feXZ3sH2uvpLqequfye6pDnn+qqw8q+Y8AAQIECBAgQIAAAQIECBAgQKBXBQ7q1Y7pFwECBAgQIECAAAECBAgQIECAQEl24yIgQIAAAQIECBAgQIAAAQIECPSugOymd+dGzwgQIECAAAECBAgQIECAAAECshvXAAECBAgQIECAAAECBAgQIECgdwVkN707N3pGgAABAgQIECBAgAABAgQIEJDduAYIECBAgAABAgQIECBAgAABAr0rILvp3bnRMwIECBAgQIAAAQIECBAgQICA7MY1QIAAAQIECBAgQIAAAQIECBDoXQHZTe/OjZ4RIECAAAECBAgQIECAAAECBGQ3rgECBAgQIECAAAECBAgQIECAQO8KyG56d270jAABAgQIECBAgAABAgQIECAgu3ENECBAgAABAgQIECBAgAABAgR6V0B207tzo2cECBAgQIAAAQIECBAgQIAAAdmNa4AAAQIECBAgQIAAAQIECBAg0LsCspvenRs9I0CAAAECBAgQIECAAAECBAjIblwDBAgQIECAAAECBAgQIECAAIHeFZDd9O7c6BkBAgQIECBAgAABAgQIECBAQHbjGiBAgAABAgQIECBAgAABAgQI9K6A7KZ350bPCBAgQIAAAQIECBAgQIAAAQKyG9cAAQIECBAgQIAAAQIECBAgQKB3BcYyu1n30/dPOviQ139rVdvDveu7f3PIGw77xordbZ/pBAIECBAgQIAAAQIECBAgQIDAgSzQMLu57aRDJh18yKSD/3baPTXjv+fr/zMoOuSwH62vlm2ff8zBb/vyNT8+7OB3X/xsdXerW6t/fNjBp9/W6tGOI0CAAAECBAgQIECAAAECBAhMCIHc7OaQScfPT98OMzj3+DDWSWY3g7ed+oa//uKKUqm05sJ3T/rgnM319VZd9fH3/82RP1+XLb3rUwcf8jf/eVe6oexBXhMgQIAAAQIECBAgQIAAAQIEJppA8+zm3Ye95ZBJB0+d+2qCZcucow4+5LC3vDt7303ikMabQUYz6S212U3jM5QQIECAAAECBAgQIECAAAECBCawQE52c/6lwcej3nXpCzHR5kuPmXTwMZdeenoqu3l5xaUnvf+v/5/y/Tj/621HfWXx5n3xGdHG4uCU6k85wdn91Pwvf/Bt4SewJh3y/lMWVhuKTvM7AQIECBAgQIAAAQIECBAgQGDiCuRkNxc/+8w5f3/IpL+/YE2FqPzy+Pm7y0FM5Xk32+/61CGHTDr4H971lVkL5s/68pH/MOngQ/76pLu2ZVT/vGrB/O++6+BDJr3u9EvnL15w1zO7S6V1PzrmsI9fELycc8FRQSVvO2d15jQvCRAgQIAAAQIECBAgQIAAAQITVyA3uymVb7SJnlgcPKW4vJ3IbtZ8722TDj7kmHnbI8UXLh04ZFL9JxbXfGYqcXvO7nlTJx18yFFXufUmgvQ7AQIECBAgQIAAAQIECBAgMOEF8rOb0qvzP35w+MTi8lOKw3twqtnN+ovfecikg09fkEhh1v0oeBrOpxbX6tZkNy+vuup7Zx3zzn/4m9f9bfhxqtR3V9VWYA8BAgQIECBAgAABAgQIECBAYCIJtJDdlEp3n/m3wROLn5r1rvjZN2OS3bx61ymHHDLpkGO+fPniBfc8s3nOSaln6EykaTBWAgQIECBAgAABAgQIECBAgEBdgZaym9LqC15/8CF/fcgbJh18UuU7p6rZTen+L/7tpIP/9lML489Mbb/qo4cEzzP+c22L6ftuEpWUgmffdPbdVbWt2EOAAAECBAgQIECAAAECBAgQOEAEWstuSuEjbMJPTpVHnoxd/jznqOAbplLPKv6br6yoJ7TqnNcdMungNxz1vfkXf2/Ouj8EX2I16XUnXTx/8dzvTX3L697gvpt6aPYRIECAAAECBAgQIECAAAECE1egxeymtHveSZPCpxSHVsnsplQq/Xlx8qu+P3XpiuyXTEXC2+757lv+V/BN4f/z+PmbS9vv/97Hw28W/5vj56xZmP7e8egUvxMgQIAAAQIECBAgQIAAAQIEJqxAw+xmwooYOAECBAgQIECAAAECBAgQIECgdwRkN70zF3pCgAABAgQIECBAgAABAgQIEMgKyG6yIl4TIECAAAECBAgQIECAAAECBHpHQHbTO3OhJwQIECBAgAABAgQIECBAgACBrIDsJiviNQECBAgQIECAAAECBAgQIECgdwRkN70zF3pCgAABAgQIECBAgAABAgQIEMgKyG6yIl4TIECAAAECBAgQIECAAAECBHpHQHbTO3OhJwQIECBAgAABAgQIECBAgACBrIDsJiviNQECBAgQIECAAAECBAgQIECgdwRkN70zF3pCgAABAgQIECBAgAABAgQIEMgKyG6yIl4TIECAAAECBAgQIECAAAECBHpHQHbTO3OhJwQIECBAgAABAgQIECBAgACBrIDsJiviNQECBAgQIECAAAECBAgQIECgdwRkN70zF3pCgAABAgQIECBAgAABAgQIEMgKyG6yIl4TIECAAAECBAgQIECAAAECBHpHQHbTO3OhJwQIECBAgAABAgQIECBAgACBrEAb2c2ePXt27ty5ffv2bdu2bfUfAQIECBAgQIAAAQIECBAgQIBApwLbtm3bvn37zp079+zZk01r0q9bym4GBwflNZ3OhfMIECBAgAABAgQIECBAgAABAs0Etm3bNjg4mE5sqq9yspuRkZFXX301rH7Xrl1DQ0MjIyPVs20RIECAAAECBAgQIECAAAECBAh0JDAyMjI0NLRr164weHn11Vfrpi7NspuRkZFXXnll69atO3bsGB4e7qgbTiJAgAABAgQIECBAgAABAgQIEGgmMDw8vGPHjq1bt77yyiu18U2z7Ca842bnzp3NqldGgAABAgQIECBAgAABAgQIECAwaoGdO3du3br11VdfzdTUMLsZHBwM77jJnOAlAQIECBAgQIAAAQIECBAgQIDAeAiEd99knn3TMLsJH07so1LjMRPqJECAAAECBAgQIECAAAECBAjUCgwPD2/dunXbtm3JovrZzZ49e7Zu3bpr167kobYJECBAgAABAgQIECBAgAABAgTGVSB8dHHyi8PrZzfhJ6yGhobGtTcqJ0CAAAECBAgQIECAAAECBAgQSAoMDQ1t3bo1+fTh+tnN9u3bt27dWvtk42RdtgkQIECAAAECBAgQIECAAAECBMZWYGRkZOvWrdu3b4+rrZ/dhA+7iQ+yQYAAAQIECBAgQIAAAQIECBAg0B2BzCNv6mc3W8v/dadDWiFAgAABAgQIECBAgAABAgQIEIgFMrGM7CaWsUGAAAECBAgQIECAAAECBAgQKF5AdlP8HOgBAQIECBAgQIAAAQIECBAgQKCRgOymkYz9BAgQIECAAAECBAgQIECAAIHi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AAECBAgQIECgmwKym25qa4sAAQIECBAgQIAAAQIECBAg0J6A7KY9L0cTIECAAAECBAgQIECAAAECBLopILvppra2CBAgQIAAAQIECBAgQIAAAQLtCchu2vNyNAECBAgQIECAAAECBAgQIECgmwKym25qa4sAAQIECBAgQIAAAQIECBAg0J6A7KY9L0cTIECAAAECBAgQIECAAAECBLopILvppra2CBAgQIAAAQIECBAgQIAAAQLtCchu2vNyNAECBAgQIECAAAECBAgQIECgmwKym25qa4sAAQIECBAgQIAAAQIECBAg0J6A7KY9L0cTIECAAAECBAgQIECAAAECBLopILvppra2CBAgQIAAAQIECBAgQIAAAQLtCchu2vNyNAECBAgQIECAAAECBAgQIECgmwKym25qa4sAAQK9JfDkln0LVw3OXbl7vH8Wrhp8csu+ePDdabcXGo2HbIMAAQIECBAgQIBAxwKym47pnEiAAIH9W+DJLfvGO7LJ1B/GN11ut8BG9+/rQ+8JECBAgAABAgR6RkB20zNToSMECBDorkB37rhJxjcLVw2WSqUut1tgo92dT60RIECAAAECBAgcsAKymwN2ag2MAAECzQWSqUrXtkulUtfaihsqqtHm/koJECBAgAABAgQItCggu2kRymEECBA40ATiaKObG0XFKN0cY9jWgXa5GA8BAgQIECBAgEBxAgVnNxs2blq77rnihl9teffg4JI7l/ZIZ6rdskWAAIFxE+h+nDF35W7ZzbjNp4p7ReDmxXdM//q5Xz7n+7+//8FR9mnjps0zZs7q7Of5zS+MsnWnEyBAgAABAr0jUGR28+jja8K/juzZs7dwkbV//FPYmUcfX1N4Z3SAAAECXRCQ3YyrQBdmcFyb2D04uPC231xy+eyl9y4bGRkZZVtr1z334kt/aVTJ0NDQ8pWPNirdj/bvHhw8edrX+voH4p//mH72tu07Oh7Cd77/47iqdje+98OfdNzu85tf+MW189rKjH5x7byn167ruEUnEiBAgAABAs0FCstuHn5sdfh3gtvv+l3zLnandGRk5LbfLA279PBjq7vTqFYIECBQoECLycWcFa+dP3f5+XOyP9+fu2L2fVtbrCQ+rLP7bv796z876rhpyZ8TvzIjrjN3o61Gf3LrMz+59Zm6dTYpqj2+45l98aW/PLDikSeeWrt9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fD/dWHp2/MGc8GE0dR+v23xwB9SatvlQWytt/GGl1s53VE8KHFDXeYsZxNge1laMMlZNF9VoD17Da//4p7rBTdjVoaGh5SvH/l9NetChsy5t277j4yeelswyTvn811/bvbuz2lo56zvf/3Gyuba2v/m9C1tpou4xzzy77hfXzqtNhZrs+cW18555dl3d2uwkQIAAAQIERi/Qo9nN6AemhrYEwiccx38pTH3nVKsVHVBr2lYH3eC48Pk1ySfdNDjQ7v1O4IC6zscqGWmrnqJilLY6OSYH73cXtw63IrB3796fXjb71C98Y/rXz11w6x2tnDKaYzb8+fnTv3jOCSed2e7PaWedvXHT5tE07VwCBAgQIECgpwRkNz01HTpDgACB7gmMSULRbiWym+5NsJYIECBAgAABAgQOFAHZzYEyk8ZBgACBNgXajV3G5HjZTZuz5HACBAgQIECAAAECJdmNi4AAAQITVGDhqsExiWNar2ThqsFSqdTldgtsdIJeWIZNgAABAgQIECAw1gKym7EWVR8BAgT2E4Ent+xrPXYZkyOf3LKvVCp1ud0CG91PLgTdJECAAAECBAgQ6HUB2U2vz5D+ESBAYPwEntyyrzt3wSxcNRhmKOFYutNuLzQ6fnOnZgIECBAgQIAAgYkjILuZOHNtpAQIECBAgAABAgQIECBAgMD+JyC72f/mTI8JECBAgAABAgQIECBAgACBiSMgu5k4c22kBAgQIECAAAECBAgQIECAwP4nILvZ/+ZMjwkQIECAAAECBAgQIECAAIGJIyC7mThzbaQECBAgQIAAAQIECBAgQIDA/icgu9n/5kyPCRAgQIAAAQIECBAgQIAAgYkjMMbZzUb/ESBAgAABAgQIECBAgAABAgQItCDQYvw0xtlNi606jAABAgQIECBAgAABAgQIECBAoBUB2U0rSo4hQIAAAQIECBAgQIAAAQIECBQjILspxl2rBAgQIECAAAECBAgQIECAAIFWBGQ3rSg5hgABAgQIECBAgAABAgQIECBQjIDsphh3rRIgQIAAAQIECBAgQIAAAQIEWhGQ3bSi5BgCBAgQIECAAAECBAgQIECAQDECB1R28+SWfQtXDc5duXtcfxauGnxyy75ipkurBAgQIECAAAECBAgQIECAwAQTOHCymye37BvXyCZTufhmgr1TDJcAAQIECBAgQIAAAQIE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nUvf8f6Pvf+jn96+45WwzvHW66zn6zdsmjFz1qlf+MZR//ypsIYXXnr5kstn19WeMXPW4jvu7qyhMTxr/cZND6x4pJWf1WueHuU1mez27+9/sBFLZv+N828dGh5OnmubAAECBAgQIECAAAECE1Og29nN+Cm3GLiM4WHjNxY1718Cjz6+Jswdlj348P7Vc70lQIAAAQIECBAgQIAAgd4XkN14VnHvX6V6SIAAAQIECBAgQIAAAQIEJq6A7EZ2M3GvfiMnQIAAAQIECBAgQIAAAQK9LyC7kd30/lWqhwQIECBAgAABAgQIECBAYOIKPWm98gAAIABJREFUHDjZzcJVg2P4LJvcqhauGpy4V42REyBAgAABAgQIECBAgAABAt0SOHCymye37MsNXMbwgCe37OvWHGmHAAECBAgQIECAAAECBAgQmLgCB052UyqVntyyrwt33yxcNSi4mbjvGCMnQIAAAQIECBAgQIAAAQLdFTigspvu0mmNAAECBAgQIECAAAECBAgQIDDuArKbcSfWAAECBAgQIECAAAECBAgQIECgYwHZTcd0TiRAgAABAgQIECBAgAABAgQIjLuA7GbciTVAgAABAgQIECBAgAABAgQIEOhYQHbTMZ0TCRAgQIAAAQIECBAgQIAAAQLjLiC7GXdiDRAgQIAAAQIECBAgQIAAAQIEOhaQ3XRM50QCBAgQIECAAAECBAgQIECAwLgLyG7GnVgDBAgQIECAAAECBAgQIECAAIGOBWQ3HdM5kQABAgQIECBAgAABAgQIECAw7gKym3En1gABAgQIECBAgAABAgQIECBAoGMB2U3HdE4kQIAAAQIECBAgQIAAAQIECIy7QBvZzb59Q3v27v3q/Jdf9431k05fN+k/1k467ZlJ//F08HPqU5NOfep/nPpk+mfN/zjVDwECBAgQIECAAAECBAgQIECAQFIglZ+EoUolYAmSlrWTTl/3um+s/+r8l/fs3btv31Ab2c3Q0PB7fvziQWds/KvT10c/z/3V6X/6q9PWVX7+449/5YcAAQIECBAgQIAAAQIECBAgQKB1gThXOf1Pf3X6c1Hksv6gMza+58cvDg0Nt5HdfO2WHQdN23TYeVvuefq1vXv37tmzd3Bwz+7Bwdd2735t9+5dr72267XdO3e95ocAAQIECBAgQIAAAQIECBAgQKAVgV2vBYlKGK3s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ECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0CP1548AAAVQklEQVR2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgRkNzlAigkQIECAAAECBAgQIECAAAECBQrIbgrE1zQBAgQIECBAgAABAgQIECBAIEdAdpMDpJgAAQIECBAgQIAAAQIECBAgUKCA7KZAfE0TIECAAAECBAgQIECAAAECBHIEZDc5QIoJECBAgAABAgQIECBAgAABAgUKyG4KxNc0AQIECBAgQIAAAQIECBAgQCBHQHaTA6SYAAECBAgQIECAAAECBAgQIFCggOymQHxNEyBAgAABAgQIECBAgAABAgRyBGQ3OUCKCRAgQIAAAQIECBAgQIAAAQIFCshuCsTXNAECBAgQIECAAAECBAgQIEAgR0B2kwOkmAABAgQIECBAgAABAgQIECBQoIDspkB8TRMgQIAAAQIECBAgQIAAAQIEcgT+v/buXrdqpI8DMHfCDew17NVQsn0qJIot6Fa0dGlSUyJRwR1AQ0H3FkhE64QkSAlKjl/N+GvGPodJTMiw5EEr1vZ82Q+F//odHx/ZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgooDspiK+pQkQIECAAAECBAgQIECAAAECBQHZTQFIMwECBAgQIECAAAECBAgQIECgosCNs5uHTz4/ePzp7cdvFU/a0gQIECBAgAABAgQIECBAgACB+yDw9uO3B48/PXzyuYl/xkt+MG6lG12nvZcnDx5/+uPvQ/FNimObAAECBAgQIECAAAECBAgQIHC7Am8/fvvj78MHjz/tvTy5QXbTtu2f//zbPa7jbwIECBAgQIAAAQIECBAgQIAAgZ8q8Oc//7Zte7Pspm3bvZcn3ZenfurJmZwAAQIECBAgQIAAAQIECBAgcG8FHj75vPfypHuW5wbZzf/8IUCAAAECBAgQIECAAAECBAgQuFuBG2Q3t/vFLbMRIECAAAECBAgQIECAAAECBAgUBWQ3RSIdCBAgQIAAAQIECBAgQIAAAQLVBGQ31egtTIAAAQIECBAgQIAAAQIECBAoCshuikQ6ECBAgAABAgQIECBAgAABAgSqCchuqtFbmAABAgQIECBAgAABAgQIECBQFJDdFIl0IECAAAECBAgQIECAAAECBAhUE5DdVKO3MAECBAgQIECAAAECBAgQIECgKCC7KRLpQIAAAQIECBAgQIAAAQIECBCoJiC7qUZvYQIECBAgQIAAAQIECBAgQIBAUUB2UyTSgQABAgQIECBAgAABAgQIECBQTUB2U43ewgQIECBAgMBKgfcHj/46eLdy8LWHHb55+tfeo7/2Xry/9pBFx3f7e4/2PywOf+/AiiHfm04bAQIECBAg8OsIhOri+avDG5+Q7ObGZAYQIECAAIH7I3D4+vmjmF/0fz97c/Ni44e0QpCxjGl+NLv58GK8qJ3BSujzI6lN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\" width=\"921\" height=\"308\"></strong></em></span></p>','2025-07-31',NULL,'2025-04-01','OPEN','Đợt đăng ký đề tài NCKH lần 3 2025Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('011_UTE_20250321_DK','2025-03-21 13:24:00.060478','211115053120159','2025-03-21 13:24:00.061456','211115053120159','scientific-profile.pdf','QĐ-2025-0123','Đợt đăng ký đề tài nghiên cứu khoa học cấp trường năm 2025.','2025-07-31',NULL,'2025-04-01','CANCELLED','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('012_UTE_20250321_DK','2025-03-21 13:48:02.480269','211115053120159','2025-05-30 20:37:22.841095','211115053120159','scientific-profile.pdf','QĐ-2025-0123','<p>Đợt đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học cấp trường năm 2025.</p>','2025-07-31',NULL,'2025-04-01','CLOSED','Đợt đăng ký đề tài NCKH lần 3 2025',NULL),('013_UTE_20250321_DK','2025-03-21 14:02:30.751440','unknown','2025-03-21 14:02:30.751440','unknown','tuan2.pdf','QD-2025-1235','<p><span style=\"color: rgb(51, 65, 85);\">Mô tả chi tiết thông báo</span></p>','2025-06-30',NULL,'2025-03-22','OPEN','Tạo đợt đăng ký mới',NULL),('014_UTE_20250321_DK','2025-03-21 14:05:24.898410','unknown','2025-04-20 16:10:44.230512','211115053120159','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1745140242/UTE-SciHub/pdf/wlg5pas03jxblqpbkeev.pdf','QD-2025-1235','<p><em><span style=\"font-size: 14pt;\">Nh&agrave; trường th&ocirc;ng b&aacute;o tới c&aacute;c Khoa, Viện, Trung t&acirc;m thuộc Trường Đại học Phenikaa việc triển khai đề t&agrave;i sinh vi&ecirc;n nghi&ecirc;n&nbsp; cứu khoa học (NCKH) năm học 2023 &ndash; 2024 như sau:</span></em></p>\n<ol>\n<li><strong>Mục đ&iacute;ch</strong></li>\n</ol>\n<ul>\n<li>Ph&aacute;t động v&agrave; triển khai hoạt động sinh vi&ecirc;n nghi&ecirc;n cứu khoa học trong to&agrave;n trường nhằm n&acirc;ng cao chất lượng đ&agrave;o tạo nguồn nh&acirc;n lực tr&igrave;nh độ cao, g&oacute;p phần ph&aacute;t hiện v&agrave; bồi dưỡng nh&acirc;n t&agrave;i.</li>\n<li>Ph&aacute;t huy t&iacute;nh năng động, s&aacute;ng tạo, khả năng nghi&ecirc;n cứu khoa học độc lập của sinh vi&ecirc;n, h&igrave;nh th&agrave;nh năng lực tự học; tự nghi&ecirc;n cứu cho sinh vi&ecirc;n. Th&ocirc;ng qua đ&oacute;, tạo điều kiện cho sinh vi&ecirc;n c&oacute; cơ hội tiếp cận v&agrave; vận dụng c&aacute;c phương ph&aacute;p nghi&ecirc;n cứu khoa học v&agrave; giải quyết một số vấn đề của khoa học v&agrave; thực tiễn.</li>\n<li>Hướng dẫn v&agrave; tạo điều kiện thuận lợi để sinh vi&ecirc;n c&oacute; thời gian thực hiện c&aacute;c c&ocirc;ng tr&igrave;nh nghi&ecirc;n cứu; tổ chức đ&aacute;nh gi&aacute; x&eacute;t chọn một số c&ocirc;ng tr&igrave;nh nghi&ecirc;n cứu khoa học sinh vi&ecirc;n c&oacute; chất lượng tốt gửi dự thi Giải thưởng KH&amp;CN d&agrave;nh cho sinh vi&ecirc;n trong c&aacute;c cơ sở gi&aacute;o dục đại học năm học 2023 &ndash; 2024.</li>\n<li>L&agrave;m cơ sở để nghiệm thu khối lượng nghi&ecirc;n cứu khoa học của c&aacute;n bộ, giảng vi&ecirc;n trong năm học 2023 &ndash; 2024.</li>\n</ul>\n<p>&nbsp;2.&nbsp;<strong>Y&ecirc;u cầu đối với đề t&agrave;i sinh vi&ecirc;n NCHKH</strong></p>\n<p>Hoạt động nghi&ecirc;n cứu khoa học của sinh vi&ecirc;n được thực hiện theo&nbsp;<em>Quy định về hoạt động nghi&ecirc;n cứu khoa học của sinh vi&ecirc;n trong c&aacute;c cơ sở gi&aacute;o dục đại học được ban h&agrave;nh theo Th&ocirc;ng tư số 26/2021/TT-BGDĐT ng&agrave;y 17/09/2021 của Bộ Gi&aacute;o dục v&agrave; Đ&agrave;o tạo&nbsp;</em>v&agrave; Quy định về hoạt động nghi&ecirc;n cứu khoa học của sinh vi&ecirc;n Trường Đại học Phenikaa. Một số y&ecirc;u cầu đối với đề t&agrave;i NCKH của sinh vi&ecirc;n như sau:</p>\n<ul>\n<li>Đề t&agrave;i sinh vi&ecirc;n NCKH ph&ugrave; hợp với khả năng, nguyện vọng của sinh vi&ecirc;n, ph&ugrave; hợp với mục ti&ecirc;u, nội dung của chương tr&igrave;nh đ&agrave;o tạo v&agrave; định hướng hoạt động KHCN của Nh&agrave; trường. Kết quả nghi&ecirc;n cứu c&oacute; gi&aacute; trị khoa học, c&oacute; t&iacute;nh mới, s&aacute;ng tạo. T&ecirc;n v&agrave; nội dung đề t&agrave;i kh&ocirc;ng được tr&ugrave;ng với đồ &aacute;n/kh&oacute;a luận tốt nghiệp hoặc c&aacute;c c&ocirc;ng tr&igrave;nh đ&atilde; được c&ocirc;ng bố.</li>\n<li>Mỗi đề t&agrave;i sinh vi&ecirc;n NCKH c&oacute; tối đa 02 c&aacute;n bộ hướng dẫn trong đ&oacute; ghi r&otilde; giảng vi&ecirc;n chịu tr&aacute;ch nhiệm ch&iacute;nh - hướng dẫn 1.</li>\n<li>Số sinh vi&ecirc;n tham gia thực hiện một đề t&agrave;i kh&ocirc;ng qu&aacute; 05 sinh vi&ecirc;n, trong đ&oacute; phải x&aacute;c định một sinh vi&ecirc;n chịu tr&aacute;ch nhiệm ch&iacute;nh l&agrave;m chủ nhiệm đề t&agrave;i.</li>\n</ul>\n<ol start=\"3\">\n<li><strong>Đăng k&yacute; đề t&agrave;i sinh vi&ecirc;n NCKH tr&ecirc;n phần mềm o.nckh</strong></li>\n</ol>\n<p>C&aacute;c đơn vị tổ chức cho giảng vi&ecirc;n v&agrave; sinh vi&ecirc;n đăng k&yacute; thực hiện đề t&agrave;i sinh vi&ecirc;n NCKH năm học 2023-2024 tr&ecirc;n phần mềm o.nckh theo c&aacute;c bước cụ thể như sau:</p>\n<p>+&nbsp;<strong>Bước 1</strong>: C&aacute;c đơn vị triển khai th&ocirc;ng b&aacute;o về việc đăng k&yacute; đề t&agrave;i sinh vi&ecirc;n nghi&ecirc;n cứu khoa học năm học 2023-2024 của Nh&agrave; trường tới giảng vi&ecirc;n v&agrave; sinh vi&ecirc;n.</p>\n<p>+&nbsp;<strong>Bước 2</strong>: C&aacute;c đơn vị tiến h&agrave;nh họp x&eacute;t chọn những đề t&agrave;i đảm bảo chất lượng v&agrave; ph&acirc;n c&ocirc;ng giảng vi&ecirc;n hướng dẫn.</p>\n<p>+&nbsp;<strong>Bước 3</strong>: Giảng vi&ecirc;n hướng dẫn cập nhật th&ocirc;ng tin sơ bộ của đề t&agrave;i l&ecirc;n phần mềm o.nckh từ t&agrave;i khoản của giảng vi&ecirc;n hướng dẫn. Lưu &yacute;, nếu đề t&agrave;i c&oacute; 02 giảng vi&ecirc;n hướng dẫn, th&ocirc;ng tin đề t&agrave;i được cập nhật l&ecirc;n phần mềm từ t&agrave;i khoản của giảng vi&ecirc;n hướng dẫn ch&iacute;nh.</p>\n<p>+&nbsp;<strong>Bước 4</strong>: L&atilde;nh đạo đơn vị đăng nhập duyệt th&ocirc;ng tin sơ bộ về đề t&agrave;i tr&ecirc;n phần mềm.</p>\n<p>+&nbsp;<strong>Bước 5</strong>: Giảng vi&ecirc;n hướng dẫn cập nhật th&ocirc;ng tin sinh vi&ecirc;n thực hiện đối với c&aacute;c đề t&agrave;i c&oacute; trạng th&aacute;i &ldquo;Đ&atilde; duyệt th&ocirc;ng tin sơ bộ&rdquo;.</p>\n<p>+&nbsp;<strong>Bước 6</strong>: L&atilde;nh đạo đơn vị duyệt th&ocirc;ng tin sinh vi&ecirc;n thực hiện đối với c&aacute;c đề t&agrave;i c&oacute; trạng th&aacute;i &ldquo;Chưa được duyệt th&agrave;nh vi&ecirc;n&rdquo;. Sau đ&oacute; l&atilde;nh đạo đơn vị xuất file b&aacute;o c&aacute;o DANH S&Aacute;CH SINH VI&Ecirc;N ĐĂNG K&Yacute; THỰC HIỆN ĐỀ T&Agrave;I NĂM HỌC 2023-2024 (XEM&nbsp;<a href=\"https://phenikaa-uni.edu.vn:3600/pu/vi/bmsvnckh-02-dssvdk.docx\" target=\"_blank\" rel=\"noopener\">TẠI Đ&Acirc;Y</a><strong>),&nbsp;</strong>in danh s&aacute;ch, k&yacute; x&aacute;c nhận v&agrave; gửi về ph&ograve;ng KHCN trước 17h00 ng&agrave;y&nbsp;<strong>25/09/2023</strong>.</p>\n<p>Căn cứ v&agrave;o danh s&aacute;ch đề t&agrave;i sinh vi&ecirc;n NCKH do c&aacute;c đơn vị đề xuất, ph&ograve;ng KHCN tổng hợp, r&agrave; so&aacute;t, tr&igrave;nh Hiệu trưởng ra quyết định giao nhiệm vụ cho giảng vi&ecirc;n hướng dẫn v&agrave; th&agrave;nh vi&ecirc;n c&aacute;c nh&oacute;m nghi&ecirc;n cứu thực hiện đề t&agrave;i.</p>\n<ol start=\"4\">\n<li><strong>Tổ&nbsp;</strong><strong>chức thực hiện đề t&agrave;i sinh vi&ecirc;n NCKH</strong>\n<ul>\n<li>Thời gian thực hiện đề t&agrave;i SVNCKH từ th&aacute;ng 10/2023 đến hết th&aacute;ng 3/2024.</li>\n<li>Nh&agrave; trường tổ chức kiểm tra tiến độ thực hiện đề t&agrave;i trong th&aacute;ng 1/2024 v&agrave; giải quyết c&aacute;c đơn đề nghị thay đổi, điều chỉnh t&ecirc;n đề t&agrave;i, đơn dừng việc thực hiện đề t&agrave;i của sinh vi&ecirc;n (nếu c&oacute;). Sau thời điểm c&aacute;c đơn vị nộp b&aacute;o c&aacute;o tiến độ, mọi đề nghị thay đổi, điều chỉnh của sinh vi&ecirc;n kh&ocirc;ng được tiếp nhận, giải quyết.</li>\n<li>Việc nghiệm thu đề t&agrave;i: thực hiện trong th&aacute;ng 4/2024.</li>\n</ul>\n</li>\n</ol>\n<p>Nh&agrave; trường đề nghị Trưởng c&aacute;c đơn vị triển khai th&ocirc;ng b&aacute;o n&agrave;y đến to&agrave;n thể giảng vi&ecirc;n, sinh vi&ecirc;n trong đơn vị đồng thời tổ chức, triển khai hoạt động sinh vi&ecirc;n NCKH theo đ&uacute;ng hướng dẫn tr&ecirc;n.</p>\n<p>T&agrave;i liệu đ&iacute;nh k&egrave;m:</p>\n<p>1. Th&ocirc;ng b&aacute;o ban h&agrave;nh xem <a href=\"https://qldt.ute.udn.vn/thong-bao/thong-bao-ve-thoi-khoa-bieu-hoc-tap-cac-lop-nang-cao-nang-luc-va-ky-nang-cho-sinh-vien\">tại đ&acirc;y</a></p>','2025-07-31',NULL,'2025-03-22','OPEN','Thông báo về việc triển khai đề tài sinh viên NCKH năm học 2024 - 2025','UTE-SciHub/pdf/wlg5pas03jxblqpbkeev.pdf'),('015_UTE_20250324_DK','2025-03-24 07:30:32.887292','unknown','2025-04-09 18:42:27.026141','211115053120159','6dc68445586c49cb9520a938deb0f0af.pdf','QD-20250324-015','<p><strong>Nhằm khuyến khích sinh viên nghiên cứu khoa học và vận dụng những kiến thức đã học vào giải quyết những vấn đề thực tiễn của cuộc sống, đồng thời phát huy năng lực tư duy sáng tạo, khả năng làm việc độc lập, làm việc theo nhóm và hình thành năng lực tự học, tự nghiên cứu cho sinh viên, Khoa TT-TV thông báo tới các bạn sinh viên về việc tổ chức hoạt động NCKH năm 2024-2025 như sau:</strong></p><p>- Thời gian tổ chức thực hiện: tháng 9/2024 đến tháng 4/2025</p><p>- Thời gian nộp đề cương nghiên cứu về Khoa, xét duyệt hướng nghiên cứu: 23-29/10/2024 (Các mốc thời gian cụ thể xin xem file đính kèm)</p><p>- Một số lợi ích của việc NCKH:</p><p>+ Được thực hành phương pháp nghiên cứu trong việc giải quyết các vấn đề khoa học, hiểu biết về lĩnh vực nghiên cứu. Các kỹ năng này hỗ trợ sinh viên rèn luyện kỹ năng làm các bài tiểu luận hết môn và khóa luận tốt nghiệp.</p><p>+&nbsp;Đối với các đề tài NCKH đạt giải cấp Trường, các bạn sinh viên thực hiện có cơ hội được xét lấy điểm cho một môn học có nội dung phù hợp với đề tài.</p><p>+ Việc thực hiện NCKHSV là một trong những tiêu chí để đánh giá kết quả rèn luyện của sinh viên, có thể là tiêu chí xét duyệt của một số loại học bổng và một số hoạt động khác.</p><p>- Hướng dẫn NCKH của GV năm học 2024-2025 :&nbsp;<a href=\"https://qldt.ute.udn.vn/sinh-vien\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"background-color: transparent; color: rgb(20, 105, 153);\">Tại Đây</a></p><p>Đây là một hoạt động rất bổ ích, mong các bạn sinh viên nhiệt tình tham gia.</p>','2025-08-31',NULL,'2025-03-25','CLOSED','THÔNG BÁO ĐĂNG KÝ ĐỀ TÀI NGHIÊN CỨU KHOA HỌC SINH VIÊN NĂM HỌC 2024-2025',NULL),('016_UTE_20250408_DK','2025-04-08 22:18:25.718444','211115053120159','2025-04-16 19:18:59.892400','211115053120158','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1744805533/UTE-SciHub/pdf/spwvcftjcngyy5tcodoa.pdf','QD-20250408-001','<p>Nhằm khuyến khích Thầy Cô nghiên cứu khoa học cũng như góp phần gia tăng công bố, Ban Giám hiệu Trường đã phê duyệt cho đăng ký đề tài NCKH cấp Trường năm 2024 đợt 2. Thời gian thực hiện dự kiến từ 11/2024 đến 11/2025, thời hạn nghiệm thu và hoàn tất các thủ tục thanh quyết toán kinh phí đến 12/2025.</p><p>Kính đề nghị Ban Chủ nhiệm Khoa, Trưởng Phòng thí nghiệm, Giám đốc Trung tâm thông báo đến các cán bộ giảng dạy và nghiên cứu viên tại đơn vị về việc nộp hồ sơ đăng ký thực hiện.</p><p><strong style=\"background-color: transparent;\">A. Loại hình đề tài nghiên cứu khoa học công nghệ:</strong></p><p><strong style=\"background-color: transparent;\">1. Đề tài định hướng nghiên cứu:</strong></p><p>– Sản phẩm đề tài có ít nhất 01 bài báo được công bố (hoặc được chấp nhận đăng) trên các tạp chí khoa học hay kỷ yếu hội nghị trong danh mục Scopus/Web of Science hiện hành; hoặc kỷ yếu hội nghị quốc tế (có phản biện) được xuất bản bởi ACM, Springer, IEEE hoặc tương đương (áp dụng cho lĩnh vực Công nghệ Thông tin, Điện tử – Viễn thông).</p><p>–&nbsp;Đối với&nbsp;các bài báo sẽ công bố trong kỷ yếu hội nghị hay tạp chí quốc tế có uy tín trong lĩnh vực, nhưng chưa có trong danh mục Scopus/Web of Science hiện hành, chủ nhiệm đề tài cần có văn bản đề xuất từ Khoa, Phòng thí nghiệm, Trung tâm để Ban Giám hiệu xem xét.</p><p><strong style=\"background-color: transparent;\">2. Đề tài định hướng ứng dụng:</strong></p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;– Sản phẩm đề tài có ít nhất 01 đăng ký sáng chế/giải pháp hữu ích (được chấp nhận đơn hợp lệ); hoặc bản quyền tác giả (đã có giấy chứng nhận).</p><p><strong style=\"background-color: transparent;\">B. Kinh phí:</strong></p><p><strong style=\"background-color: transparent;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>Các đề tài được thực hiện do nguồn kinh phí nghiên cứu khoa học công nghệ từ Trường có kinh phí&nbsp;<strong style=\"background-color: transparent;\">tối đa</strong>&nbsp;cho mỗi đề tài là&nbsp;<strong style=\"background-color: transparent;\">50 triệu đồng;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></p><p><strong style=\"background-color: transparent;\">C. Tiêu chuẩn đối với đề tài cấp Trường:</strong></p><p>– Thời gian thực hiện: 12 tháng (chưa tính thời gian nghiệm thu);</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;– Đề tài nghiên cứu phù hợp định hướng nghiên cứu khoa học công nghệ của Trường, Khoa, Trung tâm, Phòng thí nghiệm;</p><p>– Tính khoa học, tính thực tiễn, tính khả thi của đề tài;</p><p>– Tính không trùng lắp của đề tài.</p><p><strong style=\"background-color: transparent;\">D. Tiêu chuẩn đối với chủ nhiệm đề tài:</strong></p><p>– Chủ nhiệm là viên chức, người lao động thuộc Trường;</p><p>– Có chuyên môn phù hợp với đề tài đăng ký, có trình độ Cử nhân Đại học, Thạc sĩ, hoặc Tiến sĩ;</p><p>– Không nợ đề tài cũ. Nếu có đề tài chưa nghiệm thu thì phải hoàn tất các hồ sơ nghiệm thu và quyết toán;</p><p>–&nbsp;Trong thời gian nhận thực hiện đề tài không có kế hoạch đi học tập dài hạn ở nước ngoài hoặc chuyển đổi công tác;</p><p>– Không đồng thời chủ trì 02 đề tài cùng cấp;</p><p>– Không bị kỷ luật hoặc đã hết hạn kỷ luật.</p><p><strong style=\"background-color: transparent;\">E. Đăng ký thực hiện:</strong></p><p>+ Hồ sơ đăng ký gồm: 01 file Thuyết minh đề cương (theo mẫu T01),&nbsp;Lý lịch Khoa học của Chủ nhiệm&nbsp;và&nbsp;Thành viên nộp trực tuyến tại:</p><p><a href=\"https://oms-research.hcmus.edu.vn/\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(0, 63, 136); background-color: transparent;\">https://oms-research.hcmus.edu.vn</a></p><p>Từ năm 2024, các đề tài cấp Trường sẽ được đăng ký, phản biện, quản lý, gia hạn, đăng ký và tiến hành nghiệm thu trên hệ thống này.</p><p>&nbsp;&nbsp;+ Thời gian nộp hồ sơ: Từ ngày ra thông báo đến hết ngày&nbsp;10/10/2024.</p><p>&nbsp;<strong style=\"background-color: transparent;\">Lưu ý:</strong>&nbsp;Đề tài NCKH cấp Trường&nbsp;<strong style=\"background-color: transparent;\">không yêu cầu sản phẩm đào tạo.&nbsp;</strong>Do đó,<strong style=\"background-color: transparent;\">&nbsp;</strong>Chủ nhiệm đề tài vui lòng cân nhắc kỹ trước khi đăng ký vì sau khi đề tài được duyệt, các sản phẩm đã đăng ký&nbsp;<strong style=\"background-color: transparent;\">phải có đầy đủ</strong>&nbsp;thì đề tài mới được tiến hành nghiệm thu.</p>','2025-07-31',NULL,'2025-04-09','OPEN','THÔNG BÁO ĐĂNG KÝ ĐỀ TÀI NCKH CẤP TRƯỜNG NĂM 2024 (ĐỢT 2 – THÁNG 9/2024)','UTE-SciHub/pdf/spwvcftjcngyy5tcodoa.pdf'),('017_UTE_20250416_DK','2025-04-16 19:26:20.886989','211115053120158','2025-04-30 12:44:40.230057','211115053120159','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1744806380/UTE-SciHub/pdf/gc8dxh4jhke57jf4jw1v.pdf','QĐ-2025-04-022','<p><span style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000;\">Ph&ograve;ng Khoa học C&ocirc;ng nghệ ph&aacute;t h&agrave;nh&nbsp;</span><strong style=\"color: #000000;\">Th&ocirc;ng b&aacute;o đăng k&yacute; đề t&agrave;i Khoa học C&ocirc;ng nghệ cấp Trường v&agrave; Chương tr&igrave;nh Nghi&ecirc;n cứu Khoa học loại đặc biệt (th&iacute; điểm) năm học 2024 - 2025 (Đợt 1)</strong><span style=\"color: #000000;\">, chi tiết như sau:</span></span></p>\n<p><span style=\"color: #000000; font-family: \'times new roman\', times;\">1. C&aacute;n bộ - Giảng vi&ecirc;n - Nh&acirc;n vi&ecirc;n chuẩn bị hồ sơ Đề t&agrave;i Khoa học C&ocirc;ng nghệ cấp Trường theo mẫu, gồm:&nbsp;</span></p>\n<ul>\n<li><span style=\"color: #000000; font-family: \'times new roman\', times;\">Phiếu đăng k&yacute;: Mẫu (BM03/QT01/KHCN);</span></li>\n<li><span style=\"color: #000000; font-family: \'times new roman\', times;\">Thuyết minh đề t&agrave;i KHCN cấp Trường: Mẫu (BM04/QT01/KHCN)</span></li>\n<li><span style=\"color: #000000; font-family: \'times new roman\', times;\">L&yacute; lịch khoa học của chủ nhiệm: Mẫu (BM05/QT01/KHCN).</span></li>\n</ul>\n<p><span style=\"color: #000000; font-family: \'times new roman\', times;\">2. C&aacute;n bộ - Giảng vi&ecirc;n - Nh&acirc;n vi&ecirc;n chuẩn bị hồ sơ Đề t&agrave;i thuộc Chương tr&igrave;nh Nghi&ecirc;n cứu Khoa học loại đặc biệt (th&iacute; điểm năm 2024-2027) c&oacute; kinh ph&iacute; nằm ngo&agrave;i hướng dẫn lập dự to&aacute;n, đề xuất theo mẫu:</span></p>\n<ul>\n<li style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000; font-family: \'times new roman\', times;\">Phiếu đề xuất: Mẫu (TĐ01/KHCN)</span></li>\n<li style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000; font-family: \'times new roman\', times;\">Thuyết minh chương tr&igrave;nh NCKH loại đặc biệt v&agrave; Dự to&aacute;n: Mẫu (TĐ02/KHCN)</span></li>\n<li style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000; font-family: \'times new roman\', times;\">L&yacute; lịch khoa học của tất cả th&agrave;nh vi&ecirc;n: Mẫu (TĐ03/KHCN)</span></li>\n</ul>\n<p><span style=\"color: #000000; font-family: \'times new roman\', times;\">3. Khoa/Viện/Trung t&acirc;m tổng hợp danh s&aacute;ch đề t&agrave;i Khoa học C&ocirc;ng nghệ cấp Trường v&agrave; Chương tr&igrave;nh Nghi&ecirc;n cứu Khoa học loại đặc biệt (th&iacute; điểm) theo mẫu:</span></p>\n<p><span style=\"color: #000000; font-family: \'times new roman\', times;\">- <strong>Danh s&aacute;ch đề t&agrave;i: Mẫu (BM06/QT01/KHCN).</strong></span></p>\n<p><span style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000;\">Đối với c&aacute;c biểu mẫu gồm BM03/QT01/KHCN; BM04/QT01/KHCN; BM05/QT01/KHCN; TĐ01/KHCN; TĐ02/KHCN; TĐ03/KHCN v&agrave; BM06/QT01/KHCN Khoa/Viện tổng hợp v&agrave;&nbsp;</span><strong style=\"color: #000000;\">CHỈ</strong><span style=\"color: #000000;\">&nbsp;gửi bản mềm về địa chỉ email&nbsp;</span><a style=\"color: blue;\" href=\"mailto:ntt.trang@hutech.edu.vn\" target=\"_blank\" rel=\"noopener noreferrer\">ntt.trang@hutech.edu.vn</a></span></p>\n<p>&nbsp;</p>\n<p><span style=\"color: #000000; font-family: \'times new roman\', times;\">Ri&ecirc;ng với biểu mẫu BM06/QT01/KHCN, Khoa/Viện tổng hợp v&agrave; gửi bản cứng c&oacute; chữ k&yacute; l&atilde;nh đạo về Ph&ograve;ng Khoa học C&ocirc;ng nghệ (B-02.01).</span></p>\n<p><span style=\"font-family: \'times new roman\', times;\"><span style=\"color: #000000;\">Thời hạn đăng k&yacute;, nộp hồ sơ:&nbsp;</span><strong style=\"color: #0000ff;\">đến hết ng&agrave;y 15/11/2024</strong><strong style=\"color: #000000;\">.</strong></span></p>','2025-11-15',NULL,'2025-04-17','OPEN','Thông báo Đăng ký đề tài Khoa học Công nghệ cấp Trường và Chương trình Nghiên cứu Khoa học loại đặc biệt (thí điểm) năm học 2024 - 2025 (Đợt 1)','UTE-SciHub/pdf/gc8dxh4jhke57jf4jw1v.pdf');
/*!40000 ALTER TABLE `tbl_registration_periods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_research_fields`
--

DROP TABLE IF EXISTS `tbl_research_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_research_fields` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_research_fields`
--

LOCK TABLES `tbl_research_fields` WRITE;
/*!40000 ALTER TABLE `tbl_research_fields` DISABLE KEYS */;
INSERT INTO `tbl_research_fields` VALUES (1,'2025-04-17 20:47:05.733564','211115053120158','2025-04-30 19:53:40.493346','211115053120159',_binary '\0','Lĩnh vực Nông Lâm','Nông Lâm'),(2,'2025-04-17 20:54:36.479574','211115053120158','2025-04-30 19:53:15.795736','211115053120159',_binary '\0','Lĩnh vực nghiên cứu môi trường','Môi trường'),(3,'2025-04-17 21:51:31.658616','211115053120158','2025-04-30 19:53:30.945589','211115053120159',_binary '\0','Lĩnh vực kinh tế - tài chính','Kinh tế; XH-NV'),(4,'2025-04-17 23:01:09.425863','211115053120158','2025-04-30 19:52:57.430859','211115053120159',_binary '\0','Nghiên cứu ứng dụng, kỹ thuật','Kỹ thuật'),(6,'2025-04-30 19:51:05.331361','211115053120159','2025-04-30 19:51:05.331361','211115053120159',_binary '\0','Nghiên cứu về lĩnh vực tự nhiê','Tự nhiên'),(7,'2025-04-30 19:53:53.744380','211115053120159','2025-04-30 19:53:53.744380','211115053120159',_binary '\0','An toàn lao động','ATLĐ'),(8,'2025-04-30 19:54:06.970587','211115053120159','2025-04-30 19:54:06.970587','211115053120159',_binary '\0','Lĩnh vực giáo dục','Giáo dục'),(9,'2025-04-30 19:54:16.976924','211115053120159','2025-04-30 19:54:16.976924','211115053120159',_binary '\0','Lĩnh vực y dược','Y Dược'),(10,'2025-04-30 19:54:28.074233','211115053120159','2025-04-30 19:54:28.074233','211115053120159',_binary '\0','Sở hữu trí tuệ','Sở hữu  trí tuệ');
/*!40000 ALTER TABLE `tbl_research_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_research_types`
--

DROP TABLE IF EXISTS `tbl_research_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_research_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `del_flag` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_research_types`
--

LOCK TABLES `tbl_research_types` WRITE;
/*!40000 ALTER TABLE `tbl_research_types` DISABLE KEYS */;
INSERT INTO `tbl_research_types` VALUES (1,'2025-04-17 23:03:46.919711','211115053120158','2025-05-10 23:09:21.641920','211115053120159',_binary '\0','Nghiên cứu ứng dụng','Nghiên cứu ứng dụng'),(2,'2025-04-17 23:06:17.227012','211115053120158','2025-04-23 23:25:43.245649','211115053120159',_binary '\0','Nghiên cứu triển khai','Nghiên cứu triển khai'),(3,'2025-04-18 20:05:42.010477','211115053120158','2025-04-20 11:32:11.399700','211115053120159',_binary '\0','Nghiên cứu cơ bản','Nghiên cứu cơ bản'),(4,'2025-04-23 23:24:49.766318','211115053120159','2025-05-10 23:09:15.614013','211115053120159',_binary '\0','Test nghiên cứu test nghiên cứu','Nghiên cứu test');
/*!40000 ALTER TABLE `tbl_research_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_reviews`
--

DROP TABLE IF EXISTS `tbl_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `comments` text,
  `del_flag` bit(1) DEFAULT NULL,
  `council_id` bigint DEFAULT NULL,
  `milestone_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKifks2iypj431gw05wp210fsh9` (`council_id`),
  KEY `FKiabvo71gut59yy40fynvk06vm` (`milestone_id`),
  CONSTRAINT `FKiabvo71gut59yy40fynvk06vm` FOREIGN KEY (`milestone_id`) REFERENCES `tbl_milestones` (`id`),
  CONSTRAINT `FKifks2iypj431gw05wp210fsh9` FOREIGN KEY (`council_id`) REFERENCES `tbl_councils` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_reviews`
--

LOCK TABLES `tbl_reviews` WRITE;
/*!40000 ALTER TABLE `tbl_reviews` DISABLE KEYS */;
INSERT INTO `tbl_reviews` VALUES (1,NULL,NULL,NULL,NULL,'đồ án m như qq',_binary '\0',2,2),(2,NULL,NULL,NULL,NULL,'đồ án m như qq 2',_binary '\0',2,2),(3,'2025-05-18 21:53:51.635573','211115053120159','2025-05-18 21:53:51.635573','211115053120159','Đánh giá giai đoạn 2: Ok nha tr',_binary '\0',2,3),(4,'2025-05-18 22:56:29.675568','211115053120134','2025-05-18 23:29:08.934373','211115053120134','Vui lòng báo cáo tiến độ theo giai đoạn để hội đồng có thê theo dõi!Vui lòng báo cáo tiến độ theo giai đoạn để hội đồng có thê theo dõi!Vui lòng báo cáo tiến độ theo giai đoạn để hội đồng có thê theo dõi!',_binary '\0',2,4),(5,'2025-05-18 23:08:00.671151','211115053120134','2025-05-18 23:08:00.671151','211115053120134','okkokokokokokkokoko',_binary '\0',2,4),(8,'2025-05-20 21:37:38.930445','doe','2025-05-20 21:37:38.930445','doe','ok nha troiwfiiiiii',_binary '\0',2,4),(10,'2025-05-20 21:57:53.374045','doe','2025-05-20 21:57:53.374045','doe','test send email',_binary '\0',2,2),(11,'2025-05-20 22:06:21.597429','doe','2025-05-20 22:06:21.597429','doe','lại test gửi email',_binary '\0',2,2),(12,'2025-05-30 21:18:01.296416','21115053120122','2025-05-30 21:18:01.296416','21115053120122','Triển khai thực hiện báo cáo từng phần cho giai đoạn 1',_binary '\0',3,5);
/*!40000 ALTER TABLE `tbl_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_roles`
--

DROP TABLE IF EXISTS `tbl_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` enum('ADMIN','TEACHER','STUDENT','BGH','PQLKHHTQT','BCNKHOA','COUNCIL_MEMBER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_roles`
--

LOCK TABLES `tbl_roles` WRITE;
/*!40000 ALTER TABLE `tbl_roles` DISABLE KEYS */;
INSERT INTO `tbl_roles` VALUES (1,'ADMIN'),(2,'TEACHER'),(3,'STUDENT'),(4,'BGH'),(5,'PQLKHHTQT'),(6,'BCNKHOA'),(7,'COUNCIL_MEMBER');
/*!40000 ALTER TABLE `tbl_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_token`
--

DROP TABLE IF EXISTS `tbl_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expired` bit(1) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER','ACCESS','REFRESH') DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1ooyji95qv850va7f42p1am5p` (`user_id`),
  CONSTRAINT `FK1ooyji95qv850va7f42p1am5p` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=493 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_token`
--

LOCK TABLES `tbl_token` WRITE;
/*!40000 ALTER TABLE `tbl_token` DISABLE KEYS */;
INSERT INTO `tbl_token` VALUES (315,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVl8xMTFfVVRFQHN2LnV0ZS51ZG4udm4iLCJpYXQiOjE3NDY4ODAxMjQsImV4cCI6MTc0Njk2NjUyNH0.euDMyk0kWwwFfMURjCxn9syOKrSC_kuCAosNTxYquEo','ACCESS','111'),(316,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVl8xMTFfVVRFQHN2LnV0ZS51ZG4udm4iLCJpYXQiOjE3NDY4ODAxMjQsImV4cCI6MTc0NzEzOTMyNH0.W5qISuNJ40wb8j7b7qe4hCe4nsdUiFH1rMXd9wgs2GM','REFRESH','111'),(325,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ2OTQ4NjI2LCJleHAiOjE3NDcwMzUwMjZ9.Oz2Qdwj7RGW2mXNkp3rvL9uSEgAcq8t62CTY3Pza-cA','ACCESS','1001'),(326,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ2OTQ4NjI2LCJleHAiOjE3NDcyMDc4MjZ9.-CBCgXJ0azCEjn7fx2IbhpKMndR5g9vgnD3wXzD2dxw','REFRESH','1001'),(337,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZHRAdXRlLnVkbi52biIsImlhdCI6MTc0NzQ4ODExMCwiZXhwIjoxNzQ3NTc0NTEwfQ.6T1n27HWjfvXxGjBY8RoHYnU2smsgCoihSblYRlbRSc','ACCESS','3'),(338,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZHRAdXRlLnVkbi52biIsImlhdCI6MTc0NzQ4ODExMCwiZXhwIjoxNzQ3NzQ3MzEwfQ.6ljxEVfy8iDqFbG2DxFL7mLZOSjlcNtTJD1oaBl4Vjo','REFRESH','3'),(369,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcWxraGh0cXRAdXRlLnVkbi52biIsImlhdCI6MTc0ODA3NjQwMSwiZXhwIjoxNzQ4MTYyODAxfQ.wpvnSrgLWp-k9D25g486qiyoj8wKzhjCeHEYbnZglyo','ACCESS','pqlkhhtqt'),(370,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcWxraGh0cXRAdXRlLnVkbi52biIsImlhdCI6MTc0ODA3NjQwMSwiZXhwIjoxNzQ4MzM1NjAxfQ.TXElw__qIC9h_jAG_djoYSBRxn3-ypMIgaHLYBtUwEQ','REFRESH','pqlkhhtqt'),(381,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVl81QHN2LnV0ZS51ZG4udm4iLCJpYXQiOjE3NDgxMDE2OTUsImV4cCI6MTc0ODE4ODA5NX0.aySROQck5X6S1BI5uLcI45FA_E4SaANhNIlQq4GWw50','ACCESS','5'),(382,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVl81QHN2LnV0ZS51ZG4udm4iLCJpYXQiOjE3NDgxMDE2OTUsImV4cCI6MTc0ODM2MDg5NX0.84DkL6Jt-4gSer83N11rUH7yS8JANXUnsTmeXkH5Vg0','REFRESH','5'),(469,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTExMTUwNTMxMjAxMDBAc3YudXRlLnVkbi52biIsImlhdCI6MTc0OTczNzkzNSwiZXhwIjoxNzQ5ODI0MzM1fQ.MLyI7cHpucH8nDYUpmrP85ftFFZQ9iZhQoIzeH1IG4A','ACCESS','211115053120100'),(470,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTExMTUwNTMxMjAxMDBAc3YudXRlLnVkbi52biIsImlhdCI6MTc0OTczNzkzNSwiZXhwIjoxNzQ5OTk3MTM1fQ.11gxo0EdjAGyIa4-cE2Tqrhopf4YxNemaKpCLtDGQn0','REFRESH','211115053120100'),(475,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTExMTUwNTMxMjAxNTlAc3YudXRlLnVkbi52biIsImlhdCI6MTc0OTkwNzUzMCwiZXhwIjoxNzQ5OTkzOTMwfQ.ifij8ZVopn9fK-0I8RhF0ccLRkhR7brkkCgvSOfwuX8','ACCESS','211115053120159'),(476,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMTExMTUwNTMxMjAxNTlAc3YudXRlLnVkbi52biIsImlhdCI6MTc0OTkwNzUzMCwiZXhwIjoxNzUwMTY2NzMwfQ.C-pnc3DykOaP6SFQxsRzWsioPrHsbvCd5RnAn6xLNsM','REFRESH','211115053120159'),(477,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMkBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTA3NjAzLCJleHAiOjE3NDk5OTQwMDN9.QRvq-vqR8eaCaPQZAx8Q7nA0ooepdcsfo9mF-S2EVHI','ACCESS','12'),(478,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMkBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTA3NjAzLCJleHAiOjE3NTAxNjY4MDN9.QtV8tuW1OxvSuptr-jp3a5LJwkAjgYUl-SlTysdZs9I','REFRESH','12'),(487,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOEBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTEzMjgzLCJleHAiOjE3NDk5OTk2ODN9.dZ2vRHFSmIMDt8tzUS2-6OGZdfmo-Fua3WC1tQ4Zw6c','ACCESS','18'),(488,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOEBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTEzMjgzLCJleHAiOjE3NTAxNzI0ODN9.w1rmHeUM2N4xe6Q2F0TkjzPIECrSfboBxJ9dHSyyY9w','REFRESH','18'),(489,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOUBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTE0NTU0LCJleHAiOjE3NTAwMDA5NTR9.OH3VtfUDewwoTAfdCzpJx6WAMYrsNQ7txozDphty6bM','ACCESS','19'),(490,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOUBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTE0NTU0LCJleHAiOjE3NTAxNzM3NTR9.Jmc2tn4yzfIVhPx8ylzmdTxZYW0B2ynlUF7mK5b9DvY','REFRESH','19'),(491,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMkBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTE0NzI4LCJleHAiOjE3NTAwMDExMjh9.GupEGzhbs5J7avdRI__oTFHj75DgW1VoRJZproNlgSw','ACCESS','1002'),(492,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMkBzdi51dGUudWRuLnZuIiwiaWF0IjoxNzQ5OTE0NzI4LCJleHAiOjE3NTAxNzM5Mjh9.92jh50ZLZs3ZsxnGiE6cVv2SzW-DSm13i-Q0Xj4VJK0','REFRESH','1002');
/*!40000 ALTER TABLE `tbl_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_topic_applications`
--

DROP TABLE IF EXISTS `tbl_topic_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_topic_applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `motivation` text,
  `notes` varchar(255) DEFAULT NULL,
  `passed` bit(1) DEFAULT NULL,
  `plan` text,
  `status` enum('APPROVED','IN_PROGRESS','PENDING','REJECTED') DEFAULT NULL,
  `total_score` double DEFAULT NULL,
  `topic_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvxikah4htw7v862d7w1ucvwj` (`topic_id`),
  KEY `FKmuf5pfllxyhhu9xr3kycediue` (`user_id`),
  CONSTRAINT `FKmuf5pfllxyhhu9xr3kycediue` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`),
  CONSTRAINT `FKrvxikah4htw7v862d7w1ucvwj` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_topic_applications`
--

LOCK TABLES `tbl_topic_applications` WRITE;
/*!40000 ALTER TABLE `tbl_topic_applications` DISABLE KEYS */;
INSERT INTO `tbl_topic_applications` VALUES (3,'2025-05-05 21:53:03.780806','211115053120159','2025-05-17 22:36:31.217095','211115053120134','dfdfdfdfdfdfdfd',NULL,_binary '','dfdfdfdfdfdfd','APPROVED',55,'b8751fb5-2ecd-472e-a7be-462e7d372096','211115053120159'),(5,'2025-05-05 22:37:12.675001','211115053120159','2025-05-10 22:42:17.942074','211115053120159','Động lực tham gia\n',NULL,_binary '','Kế hoạch thực hiện','APPROVED',110,'0810b394-11f1-4384-8ec7-1ad6f181660c','211115053120159'),(6,'2025-05-10 13:42:39.752995','1002','2025-05-10 22:42:18.010775','211115053120159','fgdgfgdggdgfdgfdgdgfg',NULL,_binary '\0','fggdfgdfdgfgdg','REJECTED',55,'0810b394-11f1-4384-8ec7-1ad6f181660c','1002'),(7,'2025-05-24 21:44:50.057024','21115053120126','2025-05-27 23:03:45.215708','211115053120159','Đề tài xuất phát từ nhu cầu thực tế trong việc giám sát an ninh, giao thông và công nghiệp. \nCác hệ thống hiện nay còn nhiều hạn chế về hiệu suất và khả năng triển khai thực tế. \nViệc tham gia đề tài giúp nhóm nghiên cứu nâng cao kỹ năng về AI, học sâu và phát triển ứng dụng công nghệ vào đời sống.',NULL,_binary '','Kế hoạch thực hiện đề tài bao gồm các giai đoạn chính như sau:\n- Giai đoạn 1: Nghiên cứu các phương pháp phát hiện vật thể hiện đại như YOLOv5, SSD, Faster R-CNN.\n- Giai đoạn 2: Thu thập và xử lý dữ liệu hình ảnh từ nhiều nguồn khác nhau.\n- Giai đoạn 3: Huấn luyện mô hình trên tập dữ liệu được xử lý.\n- Giai đoạn 4: Tích hợp mô hình vào một hệ thống web có giao diện quản lý.\n- Giai đoạn 5: Kiểm thử và đánh giá hệ thống trong môi trường thực tế.\nDự kiến thời gian thực hiện là 12 tháng, với các mốc hoàn thành rõ ràng cho từng giai đoạn.','APPROVED',127,'40078327-5bc5-4960-877a-c81b9647417d','21115053120126'),(8,'2025-05-24 22:48:36.606882','5','2025-05-27 23:03:45.225212','211115053120159','Giải thích lý do bạn muốn làm chủ nhiệm đề tài này và động lực cá nhân.',NULL,_binary '\0','Mô tả chi tiết kế hoạch thực hiện đề tài, bao gồm các bước và thời gian dự kiến.','REJECTED',59,'40078327-5bc5-4960-877a-c81b9647417d','5'),(9,'2025-06-14 21:23:00.183852','1002','2025-06-14 22:23:25.174391','doe','Là người từng tham gia hỗ trợ giảng dạy tại các tỉnh miền núi, tôi hiểu rõ sự thiếu hụt công cụ học tập. Tôi muốn đóng góp vào việc tạo ra nền tảng hỗ trợ học tập thiết thực, hiệu quả cho các bạn sinh viên còn nhiều khó khăn.',NULL,_binary '','Đề tài được triển khai trong 3 giai đoạn:\n\nGiai đoạn 1 (Tháng 1-3): Khảo sát nhu cầu học trực tuyến của sinh viên vùng sâu; thu thập dữ liệu về điều kiện thiết bị và mạng Internet.\n\nGiai đoạn 2 (Tháng 4-8): Thiết kế và phát triển hệ thống học trực tuyến với tính năng phù hợp: giao diện đơn giản, hoạt động tốt với mạng yếu.\n\nGiai đoạn 3 (Tháng 9-12): Triển khai thử nghiệm tại 2 trường trung học tại Kon Tum và đánh giá hiệu quả sử dụng.','APPROVED',115,'e9618cf7-f89f-45cc-890a-0bb07c837c5a','1002'),(10,'2025-06-14 21:31:32.258970','18','2025-06-14 22:23:25.176403','doe','Tôi tin rằng công nghệ có thể giúp thu hẹp khoảng cách giáo dục. Việc làm chủ nhiệm giúp tôi chủ động đưa ra giải pháp phù hợp với thực tiễn và lan tỏa giá trị tích cực đến cộng đồng.',NULL,_binary '\0','Tháng 1-2: Thiết lập nhóm thực hiện đề tài, phân công công việc.\n\nTháng 3-5: Phân tích yêu cầu, thiết kế kiến trúc hệ thống dựa trên tiêu chí tiết kiệm tài nguyên, dễ triển khai.\n\nTháng 6-8: Phát triển nền tảng học trực tuyến thử nghiệm.\n\nTháng 9-10: Tổ chức lớp học thử nghiệm tại các vùng khó khăn.\n\nTháng 11-12: Báo cáo kết quả, điều chỉnh và đề xuất triển khai mở rộng.','REJECTED',110,'e9618cf7-f89f-45cc-890a-0bb07c837c5a','18');
/*!40000 ALTER TABLE `tbl_topic_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_topic_councils`
--

DROP TABLE IF EXISTS `tbl_topic_councils`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_topic_councils` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `council_id` bigint NOT NULL,
  `topic_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3u13kcsq32ihj0vy2mix8gujp` (`council_id`),
  KEY `FK6co6r3slncpmkydv529ksg6gg` (`topic_id`),
  CONSTRAINT `FK3u13kcsq32ihj0vy2mix8gujp` FOREIGN KEY (`council_id`) REFERENCES `tbl_councils` (`id`),
  CONSTRAINT `FK6co6r3slncpmkydv529ksg6gg` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_topic_councils`
--

LOCK TABLES `tbl_topic_councils` WRITE;
/*!40000 ALTER TABLE `tbl_topic_councils` DISABLE KEYS */;
INSERT INTO `tbl_topic_councils` VALUES (1,'2025-05-06 23:11:34.678230','211115053120159','2025-05-06 23:11:34.678230','211115053120159',NULL,1,'0810b394-11f1-4384-8ec7-1ad6f181660c'),(2,'2025-05-07 23:02:20.462152','211115053120159','2025-05-07 23:02:20.462152','211115053120159',NULL,2,'0810b394-11f1-4384-8ec7-1ad6f181660c'),(3,'2025-05-07 23:02:20.462152','211115053120159','2025-05-07 23:02:20.462152','211115053120159',NULL,2,'b8751fb5-2ecd-472e-a7be-462e7d372096'),(4,'2025-05-24 16:24:05.846009','211115053120159','2025-05-24 16:24:05.846009','211115053120159',NULL,3,'dfd7f292-6a41-491b-bd6c-bee1c691fb87'),(5,'2025-05-24 16:24:05.853110','211115053120159','2025-05-24 16:24:05.853110','211115053120159',NULL,3,'40078327-5bc5-4960-877a-c81b9647417d'),(6,'2025-06-01 15:01:20.775459','211115053120159','2025-06-01 15:01:20.775459','211115053120159',NULL,4,'469737e5-162d-4a21-8a5b-202d87f91712'),(7,'2025-06-14 21:14:31.918736','211115053120159','2025-06-14 21:14:31.918736','211115053120159',NULL,5,'e9618cf7-f89f-45cc-890a-0bb07c837c5a');
/*!40000 ALTER TABLE `tbl_topic_councils` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_topic_members`
--

DROP TABLE IF EXISTS `tbl_topic_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_topic_members` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `role` enum('INVESTIGATOR','MEMBER') NOT NULL,
  `topic_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt5kequa7uyakhgj4qquroenf4` (`topic_id`),
  KEY `FKhvi0jm69x158673xhpwqe4nkk` (`user_id`),
  CONSTRAINT `FKhvi0jm69x158673xhpwqe4nkk` FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`id`),
  CONSTRAINT `FKt5kequa7uyakhgj4qquroenf4` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_topic_members`
--

LOCK TABLES `tbl_topic_members` WRITE;
/*!40000 ALTER TABLE `tbl_topic_members` DISABLE KEYS */;
INSERT INTO `tbl_topic_members` VALUES (1,'2025-04-30 22:54:04.794732','211115053120159','2025-04-30 22:54:04.794732','211115053120159',_binary '\0','INVESTIGATOR','b8751fb5-2ecd-472e-a7be-462e7d372096','211115053120159'),(3,'2025-05-10 19:58:35.651908','111','2025-05-10 19:58:35.651908','111',_binary '\0','MEMBER','85882a05-f291-4d76-a079-76abeaa3a723','111'),(4,'2025-05-11 14:32:09.944118','1001','2025-05-11 14:32:09.944118','1001',_binary '\0','INVESTIGATOR','0810b394-11f1-4384-8ec7-1ad6f181660c','211115053120159'),(5,'2025-05-17 15:49:13.875453','211115053120100','2025-06-12 20:41:29.861485','21115053120126',_binary '\0','INVESTIGATOR','40078327-5bc5-4960-877a-c81b9647417d','211115053120100'),(6,'2025-05-17 22:36:31.171389','211115053120134','2025-05-17 22:36:31.171389','211115053120134',_binary '\0','INVESTIGATOR','b8751fb5-2ecd-472e-a7be-462e7d372096','211115053120134'),(7,'2025-05-24 15:32:04.698387','21115053120125','2025-05-24 15:32:04.698387','21115053120125',_binary '\0','MEMBER','dfd7f292-6a41-491b-bd6c-bee1c691fb87','21115053120125'),(8,'2025-05-27 23:03:44.895735','211115053120159','2025-06-12 20:41:29.865996','21115053120126',_binary '\0','MEMBER','40078327-5bc5-4960-877a-c81b9647417d','21115053120126'),(10,'2025-05-29 23:55:00.314944','21115053120126','2025-05-29 23:55:00.314944','21115053120126',_binary '\0','MEMBER','40078327-5bc5-4960-877a-c81b9647417d','7'),(14,'2025-06-14 22:23:25.138475','doe','2025-06-14 22:23:25.138475','doe',_binary '\0','INVESTIGATOR','e9618cf7-f89f-45cc-890a-0bb07c837c5a','1002'),(15,'2025-06-14 22:23:25.138475','12','2025-06-14 22:23:25.138475','12',_binary '\0','MEMBER','e9618cf7-f89f-45cc-890a-0bb07c837c5a','12'),(16,'2025-06-14 22:30:58.055698','1002','2025-06-14 22:30:58.055698','1002',_binary '\0','MEMBER','e9618cf7-f89f-45cc-890a-0bb07c837c5a','211115053120158');
/*!40000 ALTER TABLE `tbl_topic_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_topics`
--

DROP TABLE IF EXISTS `tbl_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_topics` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `duration_in_months` int NOT NULL,
  `end_year` int NOT NULL,
  `english_name` varchar(255) DEFAULT NULL,
  `main_content` text,
  `objective` longtext,
  `start_date` date DEFAULT NULL,
  `status` enum('DRAFT','RETURNED','WITHDRAWN','SUBMITTED','UNDER_REVIEW','NEED_REVISION','REVIEWED','WAITING_FOR_ASSIGNMENT','ASSIGNED','WAITING_FOR_APPROVAL','APPROVED','REJECTED','IN_CATALOG','IN_PROGRESS','ON_HOLD','SUSPENDED','ACCEPTANCE_REQUESTED','WAITING_FOR_ACCEPTANCE','ACCEPTED_WITH_CONDITIONS','ACCEPTED','NOT_ACCEPTED','COMPLETED','FAILED','EXPIRED','CANCELLED','ARCHIVED','DELETED') DEFAULT NULL,
  `vietnamese_name` varchar(255) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `research_field_id` int DEFAULT NULL,
  `research_type_id` int DEFAULT NULL,
  `topic_code` varchar(255) DEFAULT NULL,
  `expected_products` json DEFAULT NULL,
  `practical_applications` text,
  `approved_budget` bigint DEFAULT NULL,
  `budget_breakdown` json DEFAULT NULL,
  `expected_risks` text,
  `funding_source` varchar(255) DEFAULT NULL,
  `objectives` text,
  `total_budget` bigint DEFAULT NULL,
  `additional_notes` text,
  `commitment` bit(1) DEFAULT NULL,
  `council` varchar(255) DEFAULT NULL,
  `principal_investigator` varchar(255) DEFAULT NULL,
  `remaining_budget` bigint DEFAULT NULL,
  `urgency` text,
  `registration_period_id` varchar(255) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `approval_decision_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrcj7i8qrago4msiraabkt76tm` (`category_id`),
  KEY `FK9h6l9eutkkx2ypjkutjmi7834` (`department_id`),
  KEY `FKipvn1wx575twuuhenoiyi6jf7` (`research_field_id`),
  KEY `FKa8tok5w0xvp0s9f74opkxg57x` (`research_type_id`),
  KEY `FKmi1o44u0p8o19yrbsblqk8ju9` (`registration_period_id`),
  CONSTRAINT `FK9h6l9eutkkx2ypjkutjmi7834` FOREIGN KEY (`department_id`) REFERENCES `tbl_departments` (`id`),
  CONSTRAINT `FKa8tok5w0xvp0s9f74opkxg57x` FOREIGN KEY (`research_type_id`) REFERENCES `tbl_research_types` (`id`),
  CONSTRAINT `FKipvn1wx575twuuhenoiyi6jf7` FOREIGN KEY (`research_field_id`) REFERENCES `tbl_research_fields` (`id`),
  CONSTRAINT `FKmi1o44u0p8o19yrbsblqk8ju9` FOREIGN KEY (`registration_period_id`) REFERENCES `tbl_registration_periods` (`id`),
  CONSTRAINT `FKrcj7i8qrago4msiraabkt76tm` FOREIGN KEY (`category_id`) REFERENCES `tbl_categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_topics`
--

LOCK TABLES `tbl_topics` WRITE;
/*!40000 ALTER TABLE `tbl_topics` DISABLE KEYS */;
INSERT INTO `tbl_topics` VALUES ('0810b394-11f1-4384-8ec7-1ad6f181660c','2025-04-25 19:55:31.148617','211115053120159','2025-05-11 17:15:26.753953','211115053120159',12,2026,'Tên của đề tài bằng tiếng Anh','<p>Họ t&ecirc;n v&agrave; học vị của người đứng đầu đề t&agrave;i</p>',NULL,'2025-04-25','APPROVED','Tên của đề tài bằng tiếng Việt',1,1,2,2,'UTE-003','[{\"id\": null, \"criteria\": \"Tiêu chí đánh giá\", \"description\": \"\", \"productName\": \"Họ tên và học vị của người đứng đầu đề tài\"}, {\"id\": \"1745585007968\", \"criteria\": \"Tiêu chí đánh giá\", \"description\": \"\", \"productName\": \"Tiêu chí đánh giá\"}]','<p>Ti&ecirc;u ch&iacute; đ&aacute;nh gi&aacute;</p>',0,'[{\"id\": null, \"amount\": 1236, \"category\": \"Hạng mục\", \"description\": \"\"}]','<p>Ti&ecirc;u ch&iacute; đ&aacute;nh gi&aacute;</p>','ENTERPRISE','<p>Họ t&ecirc;n v&agrave; học vị của người đứng đầu đề t&agrave;i</p>',121212,'Continue',_binary '\0',NULL,'211115053120159@sv.ute.udn.vn',0,NULL,'001_UTE_20250319_DK',NULL,'đwdw'),('0869af8c-2679-4800-a651-8bd1a3097a14','2025-04-27 16:38:37.536570','211115053120159','2025-04-27 16:38:37.536570','211115053120159',12,2026,'Tên tiếng Anh phải có ít nhất 5 ký tự','<p>Nội dung ch&iacute;nh phải c&oacute; &iacute;t nhất 20 k&yacute; tự</p>',NULL,'2025-04-25','APPROVED','Tên tiếng Việt phải có ít nhất 5 ký tự',NULL,1,4,2,'','[{\"id\": \"1745746619452\", \"criteria\": \"Tiêu chí không được để trống\", \"description\": \"\", \"productName\": \"Tên sản phẩm không được để trống\"}]','<p><span style=\"text-decoration: underline;\"><em><strong>M&ocirc; tả ứng dụng của kết quả nghi&ecirc;n cứu v&agrave;o thực tiễn</strong></em></span></p>',121212112,'[{\"id\": null, \"amount\": 21212121, \"category\": \"Hạng mục không được\", \"description\": \"\"}, {\"id\": null, \"amount\": 12121211, \"category\": \"Hạng mục 2\", \"description\": \"\"}]','<p><span style=\"text-decoration: underline;\"><em><strong>Liệt k&ecirc; c&aacute;c rủi ro c&oacute; thể xảy ra v&agrave; biện ph&aacute;p khắc phục</strong></em></span></p>','SELF_FUNDED','<p>Mục ti&ecirc;u phải c&oacute; &iacute;t nhất 10 k&yacute; tự</p>',212212121,'',_binary '\0',NULL,'',212212121,NULL,'001_UTE_20250319_DK',NULL,NULL),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','2025-04-28 21:10:56.658323','211115053120159','2025-05-02 15:26:04.093013','nguyentrangle2006',12,2026,'Research on AI Technology Development in Vietnam','<p>Tổng quan c&ocirc;ng nghệ AI hiện nay, nghi&ecirc;n cứu c&aacute;c m&ocirc; h&igrave;nh ph&ugrave; hợp, thử nghiệm v&agrave; đ&aacute;nh gi&aacute; hiệu quả.</p>',NULL,'2025-04-28','REJECTED','Nghiên cứu và phát triển các mô hình AI phù hợp với điều kiện Việt Nam.',1,6,1,2,NULL,'[{\"id\": null, \"criteria\": \"Chính xác trên 90% trong bộ dữ liệu thử nghiệm\", \"description\": \"Ứng dụng vào chatbot, phân tích văn bản tiếng Việt.\", \"productName\": \"Mô hình AI xử lý tiếng Việt\"}]','<p>Cải thiện khả năng giao tiếp tự động trong tiếng Việt, hỗ trợ doanh nghiệp nội địa.\"</p>',450000000,'[{\"id\": null, \"amount\": 200000000, \"category\": \"Mua thiết bị\", \"description\": \"Mua server và GPU phục vụ huấn luyện AI.\"}, {\"id\": null, \"amount\": 100000000, \"category\": \"Lương cho nhóm nghiên cứu\", \"description\": \"Chi phí nhân công\"}, {\"id\": null, \"amount\": 100000000, \"category\": \"Chi phí khác\", \"description\": \"Chi phí văn phòng phẩm, đi lại, hội thảo.\"}]','<p>Thiếu dữ liệu huấn luyện ph&ugrave; hợp.</p>','GOVERNMENT','<p>Nghi&ecirc;n cứu v&agrave; ph&aacute;t triển c&aacute;c m&ocirc; h&igrave;nh AI ph&ugrave; hợp với điều kiện Việt Nam.</p>',500000000,'ok',_binary '\0',NULL,'',500000000,NULL,'001_UTE_20250319_DK','Chưa đạt yêu cầu',NULL),('258965e4-9359-4761-8126-0e56534bbe91','2025-04-24 21:58:50.094129','211115053120159','2025-05-02 23:21:18.208554','211115053120159',12,2026,'Vui lòng điền đầy đủ thông tin để đăng ký đề tài nghiên cứu khoa học','<p>Vui l&ograve;ng điền đầy đủ th&ocirc;ng tin để đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học</p>',NULL,'2025-04-24','ASSIGNED','Vui lòng điền đầy đủ thông tin để đăng ký đề tài nghiên cứu khoa học',NULL,4,4,4,'UTE-001','[{\"id\": null, \"criteria\": \"Vui lòng điền đầy đủ thông tin\", \"description\": \"\", \"productName\": \"Vui lòng điền đầy đủ thông tin để đăng ký đề tài nghiên cứu khoa học\"}]','<p>Vui l&ograve;ng điền đầy đủ th&ocirc;ng tin để đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học</p>',12121212,'[{\"id\": null, \"amount\": 12121, \"category\": \"Hạng mục\", \"description\": \"Hạng mục\"}]','<p>Vui l&ograve;ng điền đầy đủ th&ocirc;ng tin để đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học</p>','ENTERPRISE','<p>Vui l&ograve;ng điền đầy đủ th&ocirc;ng tin để đăng k&yacute; đề t&agrave;i nghi&ecirc;n cứu khoa học</p>',21212121,'Phù hợp với khoa',_binary '\0',NULL,'',21212121,NULL,'001_UTE_20250319_DK',NULL,NULL),('40078327-5bc5-4960-877a-c81b9647417d','2025-05-17 15:49:13.828823','211115053120100','2025-06-12 20:39:24.034037','211115053120159',24,2027,'Intelligent Object Detection System','<p>Nghi&ecirc;n cứu c&aacute;c thuật to&aacute;n học s&acirc;u v&agrave; m&aacute;y học để ph&aacute;t hiện v&agrave; ph&acirc;n loại vật thể trong h&igrave;nh ảnh v&agrave; video.</p>',NULL,'2025-05-17','APPROVED','Hệ thống phát hiện vật thể thông minh',1,2,4,2,NULL,'{\"training\": {\"masters\": 10, \"students\": 20}, \"commercial\": {\"details\": \"<p>Bằng s&aacute;ng chế v&agrave; sản phẩm thương mại</p>\"}, \"scientific\": {\"domestic\": 3, \"international\": 1}}','<p>Ứng dụng trong hệ thống gi&aacute;m s&aacute;t an ninh, ph&acirc;n t&iacute;ch giao th&ocirc;ng v&agrave; ng&agrave;nh c&ocirc;ng nghiệp tự động h&oacute;a.</p>',123455432,'[{\"id\": null, \"amount\": 100000000, \"category\": \"Nhân sự\", \"description\": \"Lương cho các nhà nghiên cứu và kỹ sư\"}]','<p>Ứng dụng trong hệ thống gi&aacute;m s&aacute;t an ninh, ph&acirc;n t&iacute;ch giao th&ocirc;ng v&agrave; ng&agrave;nh c&ocirc;ng nghiệp tự động h&oacute;a.</p>','GOVERNMENT','<p>Ph&aacute;t triển hệ thống ph&aacute;t hiện vật thể ch&iacute;nh x&aacute;c v&agrave; thời gian thực.</p>',200000000,'',_binary '\0',NULL,'SV_21115053120126@sv.ute.udn.vn',123455432,'<p>Đ&aacute;p ứng nhu cầu gi&aacute;m s&aacute;t tự động v&agrave; an ninh quốc gia.</p>',NULL,NULL,'QD-20250408-001'),('459a33ce-6908-45e5-948e-ee8e307dcc16','2025-04-23 23:11:31.640730','211115053120159','2025-04-23 23:11:31.640730','211115053120159',12,2026,'gdfgfgdg','<p>fgfhfhfgh</p>',NULL,'2025-04-19','REJECTED','fdgfggg',NULL,3,4,2,'fgfdgfdg','[{\"id\": \"1745424447036\", \"criteria\": \"sdfdfsf\", \"description\": \"\", \"productName\": \"sdfsfd\"}]','<p>sfdsfdsf</p>',2121212,'[{\"id\": \"1745155936883\", \"amount\": 111, \"category\": \"sdsdsdsd\", \"description\": \"11111\"}]','<p>fsdfdsfs</p>','OTHER','<p>fdfdfdf</p>',12121212,'',_binary '',NULL,'',12121212,NULL,'001_UTE_20250319_DK',NULL,NULL),('469737e5-162d-4a21-8a5b-202d87f91712','2025-04-24 21:41:03.425389','211115053120159','2025-06-12 21:47:24.866110','211115053120100',12,2026,'Đăng ký đề tài nghiên cứu khoa học','<p>Nội dung ch&iacute;nh&nbsp;<span class=\"text-destructive\">*</span></p>',NULL,'2025-04-24','ACCEPTED','Đăng ký đề tài nghiên cứu khoa học',NULL,2,3,3,'UTE-009','[{\"id\": null, \"criteria\": \"Tiêu chí đánh giá (định lượng)\", \"description\": \"Ghi chú\", \"productName\": \"Tên sản phẩm dự kiến\"}]','<p>Ứng dụng thực tiễn&nbsp;<span class=\"text-destructive\">*</span></p>',123232,'[{\"id\": null, \"amount\": 121212, \"category\": \"Hạng mục\", \"description\": \"Ghi chú\"}, {\"id\": \"1745505108474\", \"amount\": 343543, \"category\": \"Ghi chú\", \"description\": \"Hạng mục\"}]','<p>Rủi ro dự kiến</p>','OTHER','<p>Mục ti&ecirc;u&nbsp;<span class=\"text-destructive\">*</span></p>',123212,'',_binary '\0',NULL,'',123212,NULL,'001_UTE_20250319_DK',NULL,NULL),('85882a05-f291-4d76-a079-76abeaa3a723','2025-05-10 19:58:35.581594','111','2025-05-17 16:06:42.397539','211115053120159',12,2026,'Smart Fruit Classification System','<p>Hệ thống sẽ bao gồm c&aacute;c th&agrave;nh phần như thu thập dữ liệu, xử l&yacute; ảnh, huấn luyện m&ocirc; h&igrave;nh v&agrave; triển khai hệ thống.</p>',NULL,'2025-05-10','ASSIGNED','Hệ thống phân loại trái cây thông minh',1,2,4,1,NULL,'{\"training\": {\"masters\": 3, \"students\": 3}, \"commercial\": {\"details\": \"<p>ph&aacute;t triển sản phẩm thương mại h&oacute;a</p>\"}, \"scientific\": {\"domestic\": 2, \"international\": 1}}','<p>Khả năng dữ liệu kh&ocirc;ng đồng nhất v&agrave; y&ecirc;u cầu cao về sức mạnh t&iacute;nh to&aacute;n.</p>',0,'[{\"id\": null, \"amount\": 300000000, \"category\": \"Thiết bị\", \"description\": \"Mua sắm thiết bị phân loại\"}, {\"id\": null, \"amount\": 300000000, \"category\": \"Nhân sự\", \"description\": \"Chi phí nhân sự\"}, {\"id\": null, \"amount\": 300000000, \"category\": \"Khác\", \"description\": \"Chi phí vận hành\"}]','<p>Ứng dụng trong n&ocirc;ng nghiệp th&ocirc;ng minh v&agrave; tự động h&oacute;a.</p>','GOVERNMENT','<p>Ph&aacute;t triển hệ thống tự động ph&acirc;n loại tr&aacute;i c&acirc;y dựa tr&ecirc;n h&igrave;nh ảnh v&agrave; machine learning.</p>',1000000000,'',_binary '\0',NULL,'',1000000000,'<p>Ph&acirc;n loại tr&aacute;i c&acirc;y tự động l&agrave; y&ecirc;u cầu cấp thiết nhằm n&acirc;ng cao năng suất n&ocirc;ng nghiệp.</p>','017_UTE_20250416_DK',NULL,NULL),('b8751fb5-2ecd-472e-a7be-462e7d372096','2025-04-30 22:54:04.761639','211115053120159','2025-05-17 22:36:31.206425','211115053120134',12,2026,'Tên của đề tài bằng tiếng Anh','<p>M&ocirc; tả chi tiết nội dung sẽ thực hiện trong đề t&agrave;i</p>',NULL,'2025-04-30','REVIEWED','Tên của đề tài bằng tiếng Việt',1,6,8,3,NULL,'{\"training\": {\"masters\": 12, \"students\": 6}, \"commercial\": {\"details\": \"<p>M&ocirc; tả sản phẩm dự kiến, phạm vi, khả năng v&agrave; địa chỉ ứng dụng.</p>\"}, \"scientific\": {\"domestic\": 1, \"international\": 1}}','<p>M&ocirc; tả ứng dụng của kết quả nghi&ecirc;n cứu v&agrave;o thực tiễn</p>',0,'[{\"id\": null, \"amount\": 12121212, \"category\": \"Hạng mục\", \"description\": \"\"}]','','SELF_FUNDED','<p>M&ocirc; tả mục ti&ecirc;u chung v&agrave; cụ thể của đề t&agrave;i</p>',121212122,'ok đề tài đạt yêu cầu',_binary '\0',NULL,'211115053120159@sv.ute.udn.vn',121212122,'<p>M&ocirc; tả t&iacute;nh cấp thiết v&agrave; đ&oacute;ng g&oacute;p khoa học của đề t&agrave;i</p>','016_UTE_20250408_DK',NULL,NULL),('dfd7f292-6a41-491b-bd6c-bee1c691fb87','2025-05-24 15:32:04.662713','21115053120125','2025-05-24 17:36:21.463144','pqlkhhtqt',12,2026,'Development of Environmental Data Collection System','<p>Nghi&ecirc;n cứu c&aacute;c loại cảm biến, kết nối IoT, lưu trữ dữ liệu đ&aacute;m m&acirc;y v&agrave; x&acirc;y dựng dashboard hiển thị.</p>',NULL,'2025-05-24','REVIEWED','Phát triển hệ thống thu thập dữ liệu môi trườn',1,6,2,2,NULL,'{\"training\": {\"masters\": 0, \"students\": 0}, \"commercial\": {\"details\": \"<p>Triển khai hệ thống cảm biến kh&ocirc;ng kh&iacute; cho c&aacute;c trường học tại đ&ocirc; thị lớn.</p>\"}, \"scientific\": {\"domestic\": 1, \"international\": 0}}','<p>Hệ thống gi&uacute;p cảnh b&aacute;o sớm &ocirc; nhiễm v&agrave; cải thiện quy hoạch đ&ocirc; thị xanh.</p>',0,'[{\"id\": null, \"amount\": 400000000, \"category\": \"Thiết bị cảm biến\", \"description\": \"Mua 50 bộ cảm biến đo bụi mịn và khí độc\"}, {\"id\": null, \"amount\": 250000000, \"category\": \"Chi phí nhân sự\", \"description\": \"\"}]','<p>Thiết bị c&oacute; thể hỏng h&oacute;c khi triển khai ngo&agrave;i trời hoặc mất kết nối</p>','GOVERNMENT','<p>Tạo ra hệ thống cảm biến th&ocirc;ng minh thu thập dữ liệu m&ocirc;i trường theo thời gian thực.</p>',800000000,'',_binary '\0',NULL,'',800000000,'<p>&Ocirc; nhiễm kh&ocirc;ng kh&iacute; v&agrave; biến đổi kh&iacute; hậu đ&ograve;i hỏi cần c&oacute; hệ thống gi&aacute;m s&aacute;t tức thời.</p>',NULL,NULL,NULL),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','2025-06-14 20:32:45.170552','12','2025-06-14 22:23:25.165594','doe',12,2026,'Developing an Online Learning Platform for Remote Students','<p>X&acirc;y dựng hệ thống học trực tuyến nhẹ, dễ sử dụng tr&ecirc;n c&aacute;c thiết bị cũ v&agrave; mạng yếu. K&egrave;m theo nội dung học miễn ph&iacute;.</p>',NULL,'2025-06-14','IN_CATALOG','Phát triển nền tảng học trực tuyến cho sinh viên vùng sâu',1,1,8,1,'UTE-0010','{\"training\": {\"masters\": 0, \"students\": 20}, \"commercial\": {\"details\": \"<p>Phần mềm m&atilde; nguồn mở c&oacute; thể thương mại h&oacute;a dưới dạng dịch vụ.</p>\"}, \"scientific\": {\"domestic\": 1, \"international\": 0}}','<p>Triển khai tại c&aacute;c trường trung học v&ugrave;ng s&acirc;u như Quảng Nam, Kon Tum.</p>',0,'[{\"id\": null, \"amount\": 40000000, \"category\": \"Phần mềm\", \"description\": \"\"}, {\"id\": null, \"amount\": 30000000, \"category\": \"Đào tạo\", \"description\": \"\"}]','<p>Phần mềm m&atilde; nguồn mở c&oacute; thể thương mại h&oacute;a dưới dạng dịch vụ.</p>','ENTERPRISE','<p>Hỗ trợ sinh vi&ecirc;n v&ugrave;ng s&acirc;u c&oacute; điều kiện học tập trực tuyến dễ d&agrave;ng hơn.</p>',120000000,'Khoa Công nghệ số xác nhận đề tài này',_binary '\0',NULL,'example2@sv.ute.udn.vn',120000000,'<p>Cần thiết để thu hẹp khoảng c&aacute;ch học tập giữa th&agrave;nh thị v&agrave; n&ocirc;ng th&ocirc;n.</p>','016_UTE_20250408_DK',NULL,NULL);
/*!40000 ALTER TABLE `tbl_topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_users`
--

DROP TABLE IF EXISTS `tbl_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_users` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `status` enum('ACTIVE','BLOCKED','INACTIVE') DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `image_public_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj562wwmipqt96rkoqbo0jc34` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_users`
--

LOCK TABLES `tbl_users` WRITE;
/*!40000 ALTER TABLE `tbl_users` DISABLE KEYS */;
INSERT INTO `tbl_users` VALUES ('1','2025-03-30 14:35:32.512556','211115053120159','2025-03-30 14:35:32.512556','211115053120159','SV_1@sv.ute.udn.vn','$2a$10$x0T0nAhYYGFmYYYwZz8iuOXuJAb1KXYyubpIEn/9QpYFo8Hiq6r62',NULL,'Tài khoản 742227',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('10','2025-03-30 15:13:20.627931','211115053120159','2025-03-30 15:13:20.627931','211115053120159','10@sv.ute.udn.vn','$2a$10$O3npFJhw1MfcITFVkZdd6eG79DRsju6Sv1tjW3U.2tp0LGX.Awp4O',NULL,'Tài khoản 343365',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('1001','2025-04-03 21:10:28.672620','211115053120159','2025-05-11 14:30:27.392870','1001','example1@sv.ute.udn.vn','$2a$10$U.8s9B3PN1M6riHO78S3pOj4V5O0tUJcn7UC3FQDTxkx4RZnTMiTu',NULL,'Nguyễn Văn A','0901234567','2025-05-11 14:30:27.184118','ACTIVE',NULL,NULL,NULL),('1002','2025-04-03 22:22:43.673247','211115053120159','2025-06-14 22:25:28.670861','1002','example2@sv.ute.udn.vn','$2a$10$1v0T48OwjzNlRIesSeGlLObx2kgIjRUJuvFLTDg3ro4O.fbE3Tn4m',NULL,'Nguyễn Văn B','0901234566','2025-06-14 22:25:28.664886','ACTIVE',NULL,NULL,NULL),('11','2025-03-30 15:13:20.632252','211115053120159','2025-03-30 15:13:20.632252','211115053120159','11@sv.ute.udn.vn','$2a$10$jEebrfvFQvkHHlkIUZ6ho.aoUxhyGkX0gCEePw5wGbUxQpp1Bufla',NULL,'Tài khoản 490144',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('111','2025-04-01 15:04:13.155891','211115053120159','2025-05-10 19:28:44.520309','111','SV_111_UTE@sv.ute.udn.vn','$2a$10$HF9ZWtKQk0oL1AtfhlU7LeRZ1hSFFs5JWRum8j9FEAMd9/A/IDwLi',NULL,'Tài khoản 420626',NULL,'2025-05-10 19:28:44.469622','ACTIVE',NULL,NULL,NULL),('12','2025-03-30 15:13:20.635312','211115053120159','2025-06-14 20:26:43.570524','12','12@sv.ute.udn.vn','$2a$10$MmIWN39kJLbmapk1T0btX.dku7CjcXDWCfoZI9q.EH2/OTTmMxGoG',NULL,'Tài khoản 632696',NULL,'2025-06-14 20:26:43.559986','ACTIVE',NULL,NULL,NULL),('13','2025-03-30 15:13:20.637309','211115053120159','2025-03-30 15:13:20.637309','211115053120159','13@sv.ute.udn.vn','$2a$10$gjqVyjwFRu.ZvQIChRBlHurIcnFPXTKUY1B8Cl9t1ToJX5tLkPs1K',NULL,'Tài khoản 901605',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('14','2025-03-30 15:13:20.638269','211115053120159','2025-03-30 15:13:20.638269','211115053120159','14@sv.ute.udn.vn','$2a$10$6y7JsN1VTuQsCpixpCPxSOd1jpGx/Mxbx7wIeFaVMAgEaWklhiJEe',NULL,'Tài khoản 609937',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('15','2025-03-30 15:13:20.639267','211115053120159','2025-03-30 15:13:20.639267','211115053120159','15@sv.ute.udn.vn','$2a$10$SotQxcEhThx3vqPicOitceBF55M4a7qyt8zKF4/D51Z.lnlXky2VK',NULL,'Tài khoản 344871',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('16','2025-03-30 15:13:20.640307','211115053120159','2025-03-30 15:13:20.640307','211115053120159','16@sv.ute.udn.vn','$2a$10$d2GqhDblD0Panz26PPt4kebBY05lzNMpI2RGnIcRBLI8cLrCiMS4u',NULL,'Tài khoản 894544',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('17','2025-03-30 15:13:20.641811','211115053120159','2025-03-30 15:13:20.641811','211115053120159','17@sv.ute.udn.vn','$2a$10$R/0l3W/1oZj0IB1tuKvc0eXs/Quts/H.nqBoNPF0mKdWdtR1Tj1b2',NULL,'Tài khoản 438108',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('18','2025-03-30 15:13:20.643865','211115053120159','2025-06-14 22:01:23.514925','18','18@sv.ute.udn.vn','$2a$10$.Pr5XQ0D2PzheDY120jtE.PBTuWJnJd3lu1utWUWnZ8q0zDtVLmWy',NULL,'Tài khoản 506907',NULL,'2025-06-14 22:01:23.485875','ACTIVE',NULL,NULL,NULL),('19','2025-03-30 15:13:20.644822','211115053120159','2025-06-14 22:22:34.870766','19','19@sv.ute.udn.vn','$2a$10$zans2G9n5Ty2cC583pUHYOZG4NuGlHh28opV.EQuz.9STQAPK47re',NULL,'Tài khoản 220211',NULL,'2025-06-14 22:22:34.861253','ACTIVE',NULL,NULL,NULL),('2','2025-03-30 14:35:32.520598','211115053120159','2025-03-30 14:35:32.520598','211115053120159','SV_2@sv.ute.udn.vn','$2a$10$l6BRfGoaoROiPdnBCfm64uUHTuHyztEK3s1jXg1EjCg0TRSB1QaBG',NULL,'Tài khoản 580337',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('20','2025-03-30 15:08:50.554252','211115053120159','2025-03-30 15:08:50.554252','211115053120159','20@sv.ute.udn.vn','$2a$10$L4RP3iQB4WhdFESxP24s7OJF4yIANDZPjpVLZHEg.hDxESQBMemI.',NULL,'Tài khoản 241053',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('21','2025-03-30 15:14:48.301766','211115053120159','2025-03-30 15:14:48.301766','211115053120159','21@sv.ute.udn.vn','$2a$10$B42gWCu.4PIsc3GaOJrO0eCgrJ5JCllQDsoZz8l2oPMKr/7ngRiKu',NULL,'Tài khoản 464253',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('211115053120100','2025-04-16 22:23:49.705130','211115053120158','2025-06-12 21:18:55.314439','211115053120100','211115053120100@sv.ute.udn.vn','$2a$10$fOECZc6pvHBj8H185.M8UeZ/h7uQNirFwFJeinnQHJvSNBqPcW6ji','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744817028/UTE-SciHub/images/clv3w0rrghdhwjuufolk.png','Nguyễn Văn Anh','0989296540','2025-06-12 21:18:55.301397','ACTIVE',NULL,NULL,'UTE-SciHub/images/clv3w0rrghdhwjuufolk'),('211115053120134','2025-04-13 17:13:44.129414','211115053120159','2025-05-20 20:26:55.269407','211115053120134','211115053120134@sv.ute.udn.vn','$2a$10$QZpX8ICqpqNuOvDycyylsOY0Im5erfcIwyEb3ocV3YOzj6XRq5TAy','3ad85b7f20bf4563bd450272a84f6fd7.png','Lê Đại Minh Phú Qúy','0912345678','2025-05-20 20:26:55.268406','ACTIVE',NULL,NULL,NULL),('211115053120158','2025-03-18 17:52:24.433953','unknown','2025-05-07 22:29:01.576973','211115053120158','211115053120158@sv.ute.udn.vn','$2a$10$iwqZAfvF9fUeg5HCo8RFfeFMA94kCxdDMM7pMY80uL1ep5L7Mcgs2',NULL,'Tài khoản 598107',NULL,'2025-05-07 22:29:01.484818','ACTIVE',NULL,NULL,NULL),('211115053120159','2025-03-18 17:58:43.150832','unknown','2025-06-14 20:25:31.113416','211115053120159','211115053120159@sv.ute.udn.vn','$2a$10$.mZXUydGJ4euoyexPa86Vels6wTnZqUxL8djoLa2mWxzu9Dd.y4B.',NULL,'Lê Thanh Tuấn',NULL,'2025-06-14 20:25:31.001580','ACTIVE',NULL,NULL,NULL),('21115053120106','2025-04-02 11:27:39.858136','211115053120159','2025-04-02 11:27:39.858136','211115053120159','SV21115053120106@sv.ute.udn.vn','$2a$10$9W7H/iDimAy4UeweJErYYej9LHEpZvTizJ6nzT.1yrY53vGqx0Q16',NULL,'Tài khoản 597778',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('21115053120107','2025-04-02 11:27:39.887553','211115053120159','2025-06-07 17:19:25.662120','211115053120159','SV21115053120107@sv.ute.udn.vn','$2a$10$az/CgUUNfhXpfufqQEgsj.Cs/rdKI1v1Ch/N9El9P9EhuJS8zoyFO','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTUspugOXub65sbxVHOEaD-JEKC8NNWgkWhlg&s','Tài khoản 194569',NULL,NULL,'BLOCKED',NULL,NULL,NULL),('21115053120108','2025-04-02 11:27:39.889548','211115053120159','2025-05-10 23:14:10.915126','doe','SV21115053120108@sv.ute.udn.vn','$2a$10$e.8vlEIJA3QHnfhxlkdUoed6EqkJqMnzBzjlALKafG5x3H3qOnNlG',NULL,'Tài khoản 179510',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('21115053120122','2025-04-03 23:07:53.165146','211115053120159','2025-05-30 21:17:17.001176','21115053120122','21115053120122@sv.ute.udn.vn','$2a$10$LINzeMlmhM41s6j3Lxd/I.xGIGHxdQmy7iB9VjsTTr30pWkWL/R.G',NULL,'Lê Quang Luânn','0865134432','2025-05-30 21:17:16.993653','ACTIVE',NULL,NULL,NULL),('21115053120124','2025-03-30 14:38:14.674360','211115053120159','2025-03-30 14:38:14.674360','211115053120159','SV_21115053120124@sv.ute.udn.vn','$2a$10$nv1k2ZWU/kReMFrKSygdkOwmodxUJfePcbWzC1V84JP721p0wb4uq',NULL,'Tài khoản 313842',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('21115053120125','2025-03-30 14:38:14.690816','211115053120159','2025-05-24 15:25:14.833673','21115053120125','SV_21115053120125@sv.ute.udn.vn','$2a$10$J.FFE/l5ewzM8sFZ3xgvROFBeTWo3V7w3x46.mKzOFzosjzweMXWy',NULL,'Tài khoản 30683',NULL,'2025-05-24 15:25:14.830572','ACTIVE',NULL,NULL,NULL),('21115053120126','2025-03-30 14:38:14.692322','211115053120159','2025-06-12 20:40:29.634418','21115053120126','SV_21115053120126@sv.ute.udn.vn','$2a$10$l5NqHr2gorQJCNGmMekP7.tcmbYDshGpEAWbSdcvLNCUYkZZ1JZ4m',NULL,'Lê Thị Thanh Tâm',NULL,'2025-06-12 20:40:29.633417','ACTIVE',NULL,NULL,NULL),('3','2025-03-30 14:35:32.522604','211115053120159','2025-06-08 13:42:26.414462','211115053120159','ddt@ute.udn.vn','$2a$10$BcP.yT6TMhrdOphcojvcuO2yNehXWAFIO279Z3T/yGDhIMcMp2d/G','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744731988/UTE-SciHub/images/spgwyod08dvub2chwsno.jpg','Khoa Điện - Điện tử','0123456789','2025-05-17 20:21:51.030650','ACTIVE',NULL,NULL,NULL),('4','2025-03-30 14:35:32.524605','211115053120159','2025-06-14 20:44:46.595711','4','fdt@ute.udn.vn','$2a$10$25GFNgBntTaEQ1onGtjVee2DYCUUan.TbgTbtAGA/tsMBy/IjL6cu',NULL,'Khoa Công nghệ số',NULL,'2025-06-14 20:44:46.592138','ACTIVE',NULL,NULL,NULL),('5','2025-03-30 14:35:32.525603','211115053120159','2025-05-24 22:48:15.564363','5','SV_5@sv.ute.udn.vn','$2a$10$QlhWbDfDIhXcBaP8.GVQI.hPk.7iE9fnNfkQ5m06TbQxXq7x7koge',NULL,'Tài khoản 211890',NULL,'2025-05-24 22:48:15.562850','ACTIVE',NULL,NULL,NULL),('6','2025-03-30 14:50:57.484368','211115053120159','2025-05-26 22:00:30.635883','211115053120159','chemistry@ute.ud.vn','$2a$10$kcTT2OUDBzL9k1xCaUx.H.Ojf28k0c.ct5Wx1F9DpBFBc6zZQoqX.','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744732026/UTE-SciHub/images/vawydd4jpdrw4tpvsbgw.jpg','Khoa Công nghệ hóa học - Môi trường','0912345678',NULL,'ACTIVE',NULL,NULL,NULL),('7','2025-03-30 14:58:53.967104','211115053120159','2025-05-30 21:00:56.623922','7','7@sv.ute.udn.vn','$2a$10$Paf.Q7Z7o.QUNoZbL2mWMudWUlGLpb1YzkXtVQhiPs1/7uFfKbCcy',NULL,'Lê Tấn 7',NULL,'2025-05-30 21:00:56.590144','ACTIVE',NULL,NULL,NULL),('8','2025-03-30 15:08:22.759300','211115053120159','2025-03-30 15:08:22.759300','211115053120159','8@sv.ute.udn.vn','$2a$10$G1y4X3j1ncLc8sLZhZI3KeFNDf1tDETurrvWEy/n6WQlbD6p6CtiC',NULL,'Tài khoản 967403',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('9','2025-03-30 15:08:50.534174','211115053120159','2025-03-30 15:08:50.534174','211115053120159','9@sv.ute.udn.vn','$2a$10$5reASV2WNq5X4qJd1srTheJSZIFD.swBiFdVQD2vzDorNWg1oXn5K',NULL,'Tài khoản 201342',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('doe','2025-04-02 11:35:05.279738','211115053120159','2025-06-14 21:32:55.620201','doe','doe@ute.udn.vn','$2a$10$Gn/os5XLLsasGxoQOTaCnuZm4V9XnUKKZiBi/zFrgzc5aWqgn0lbK',NULL,'Lê Phuớc Chớ','0912345678','2025-06-14 21:32:55.617194','ACTIVE',NULL,NULL,NULL),('nguyentrangle2006','2025-04-23 19:37:58.944430','211115053120159','2025-05-02 14:56:41.239070','nguyentrangle2006','k.spcn@ute.udn.vn','$2a$10$fB.XYU5EP1zuPYh2TsNDF.ELxKQ/NK8OtoZWd2xP8WdP5dpxWvJAO',NULL,'	Khoa Sư phạm Công nghiệp','01234567890','2025-05-02 14:56:41.188320','ACTIVE',NULL,'FEMALE',NULL),('pqlkhhtqt','2025-05-02 14:04:13.742191','211115053120159','2025-05-24 15:46:41.818667','pqlkhhtqt','pqlkhhtqt@ute.udn.vn','$2a$10$EIgzvrsm.ZBXhQqgyrbDLOnM99rVTBj3xOsAbeRCi0kFCPM7YmbhW',NULL,'Phòng Quản lý khoa học và hợp tác quốc tế','0989296540','2025-05-24 15:46:41.793370','ACTIVE',NULL,'MALE',NULL),('sys_admin','2025-03-20 11:48:29.080522','unknown','2025-03-20 11:48:29.080522','unknown','sys_admin@sv.ute.udn.vn','$2a$10$7vkPGYbITJM3aUz8/GQM5ecKw47b/0UeBg5SBKr./9mqoznQYgXdq',NULL,'Tài khoản 104504',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('thanhtnhan','2025-04-16 22:39:13.027561','211115053120158','2025-05-02 14:47:33.555648','thanhtnhan','chemistry@ute.udn.vn','$2a$10$UUZGjDcHxyfPG66Y2FXMFuNJ5ooufFOYPs5fKTf6HO8MCe5Y4YNiS','http://res.cloudinary.com/dxrfmq2ru/image/upload/v1744817951/UTE-SciHub/images/e6ml3brniqsss3snooem.png','	Khoa Công nghệ hóa học - Môi trường	','0378315280','2025-05-02 14:47:33.554671','ACTIVE',NULL,'FEMALE','UTE-SciHub/images/e6ml3brniqsss3snooem'),('thanhtuanle','2025-03-17 21:50:46.064084','unknown','2025-03-17 21:55:38.387267','unknown','thanhtuanle','$2a$10$LkXLOFuj1NETcVEmaVtkG.MkQc5kz5trnC5kXEuyvvicanSKtF54q',NULL,'Tài khoản 918492',NULL,NULL,'ACTIVE',NULL,NULL,NULL),('thanhtuanle0209','2025-04-01 16:26:44.894680','211115053120159','2025-04-01 16:26:44.895680','211115053120159','khoaktxd.ute@gmail.com','$2a$10$6klESsITqZgMLHFSpaMjUuAo8y99iVHbsH92hYfY2uAtUguuyj99O','2206d-1561174373572434094792-crop-1561174381855253304104.png','Khoa Kỹ thuật Xây dựng','0912345678',NULL,'ACTIVE',NULL,NULL,NULL),('thanhtuanle0209999','2025-04-01 16:31:01.323667','211115053120159','2025-05-24 22:47:20.397820','thanhtuanle0209999','nlcthanh@ute.udn.vn','$2a$10$szWRXM4DIbffL.r0H4j16O.VwLvLxr/lC2cOJU1X2/Xa1z0kFLUAG',NULL,'	Khoa Cơ khí','0378315207','2025-05-24 22:47:20.358116','ACTIVE',NULL,NULL,NULL);
/*!40000 ALTER TABLE `tbl_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_files`
--

DROP TABLE IF EXISTS `topic_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_files` (
  `topic_id` varchar(255) NOT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `original_file_name` varchar(255) DEFAULT NULL,
  KEY `FK9qvlfcorcxrb0w4n6eqeu4i8k` (`topic_id`),
  CONSTRAINT `FK9qvlfcorcxrb0w4n6eqeu4i8k` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_files`
--

LOCK TABLES `topic_files` WRITE;
/*!40000 ALTER TABLE `topic_files` DISABLE KEYS */;
INSERT INTO `topic_files` VALUES ('0810b394-11f1-4384-8ec7-1ad6f181660c','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1745585728/UTE-SciHub/pdf/p3vh9jcjrub9yh7lnwiq.pdf','ERD','UTE-SciHub/pdf/p3vh9jcjrub9yh7lnwiq.pdf',NULL),('0869af8c-2679-4800-a651-8bd1a3097a14','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1745746714/UTE-SciHub/pdf/u9c2sp5o3ou4a4skumuz.pdf','erd','UTE-SciHub/pdf/u9c2sp5o3ou4a4skumuz.pdf',NULL),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1745849453/UTE-SciHub/pdf/k2ckwocwimpzzuy0wtyq.pdf','Kế hoạch chi tiết','UTE-SciHub/pdf/k2ckwocwimpzzuy0wtyq.pdf',NULL),('85882a05-f291-4d76-a079-76abeaa3a723','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1746881914/UTE-SciHub/pdf/geefqukyopvamdduqedp.pdf','Bản thảo đề cương nghiên cứu','UTE-SciHub/pdf/geefqukyopvamdduqedp.pdf','Võ Trung Hùng - 21115053120158 - Lê Thanh Tuấn - ERD.pdf'),('40078327-5bc5-4960-877a-c81b9647417d','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1747471753/UTE-SciHub/pdf/hhpo3uqlivi0pdev2obi.pdf','Bản thảo nghiên cứu','UTE-SciHub/pdf/hhpo3uqlivi0pdev2obi.pdf','Bản sao của Phần Lịch.pdf'),('dfd7f292-6a41-491b-bd6c-bee1c691fb87','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1748075524/UTE-SciHub/pdf/stwlmuczzchwu4iiayon.pdf','Sơ đồ kỹ thuật hệ thống cảm biến','UTE-SciHub/pdf/stwlmuczzchwu4iiayon.pdf','QT.01- KHCN_Quản lý và thực hiện đề tài cấp trường (1) (1).pdf'),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','http://res.cloudinary.com/dxrfmq2ru/raw/upload/v1749907964/UTE-SciHub/pdf/rxu5ldncu8kanid3do3d.pdf','Bảng khảo sát sinh viên','UTE-SciHub/pdf/rxu5ldncu8kanid3do3d.pdf','erd2.pdf');
/*!40000 ALTER TABLE `topic_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_keywords`
--

DROP TABLE IF EXISTS `topic_keywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_keywords` (
  `topic_id` varchar(255) NOT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  KEY `FKsjwiqonedws6f41fsyykwf8py` (`topic_id`),
  CONSTRAINT `FKsjwiqonedws6f41fsyykwf8py` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_keywords`
--

LOCK TABLES `topic_keywords` WRITE;
/*!40000 ALTER TABLE `topic_keywords` DISABLE KEYS */;
INSERT INTO `topic_keywords` VALUES ('258965e4-9359-4761-8126-0e56534bbe91','java'),('258965e4-9359-4761-8126-0e56534bbe91','spring'),('258965e4-9359-4761-8126-0e56534bbe91','ts'),('0810b394-11f1-4384-8ec7-1ad6f181660c','cntt'),('0810b394-11f1-4384-8ec7-1ad6f181660c','test'),('0869af8c-2679-4800-a651-8bd1a3097a14','ok'),('0869af8c-2679-4800-a651-8bd1a3097a14','okok'),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','AI'),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','trí tuệ nhân tạo'),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','nghiên cứu'),('b8751fb5-2ecd-472e-a7be-462e7d372096','ok'),('b8751fb5-2ecd-472e-a7be-462e7d372096','tesst'),('b8751fb5-2ecd-472e-a7be-462e7d372096','test ok'),('85882a05-f291-4d76-a079-76abeaa3a723','Classification '),('85882a05-f291-4d76-a079-76abeaa3a723','AI'),('40078327-5bc5-4960-877a-c81b9647417d','AI'),('40078327-5bc5-4960-877a-c81b9647417d','Machine learning'),('40078327-5bc5-4960-877a-c81b9647417d','Computer science'),('dfd7f292-6a41-491b-bd6c-bee1c691fb87','IoT'),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','E-learning'),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','giáo dục');
/*!40000 ALTER TABLE `topic_keywords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic_transfer_forms`
--

DROP TABLE IF EXISTS `topic_transfer_forms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic_transfer_forms` (
  `topic_id` varchar(255) NOT NULL,
  `transfer_form` varchar(255) DEFAULT NULL,
  KEY `FK2qkto257vtlm3mp06pjiqo8nv` (`topic_id`),
  CONSTRAINT `FK2qkto257vtlm3mp06pjiqo8nv` FOREIGN KEY (`topic_id`) REFERENCES `tbl_topics` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic_transfer_forms`
--

LOCK TABLES `topic_transfer_forms` WRITE;
/*!40000 ALTER TABLE `topic_transfer_forms` DISABLE KEYS */;
INSERT INTO `topic_transfer_forms` VALUES ('258965e4-9359-4761-8126-0e56534bbe91','Chuyển giao nghiên cứu'),('0810b394-11f1-4384-8ec7-1ad6f181660c','Chuyển giao nghiên cứu'),('0869af8c-2679-4800-a651-8bd1a3097a14','Chuyển giao nghiên cứu'),('0869af8c-2679-4800-a651-8bd1a3097a14','Chuyển giao sản phẩm'),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','Chuyển giao nghiên cứu'),('1446c27b-6fc0-4c09-8d18-9a6bf6030b5c','Chuyển giao công nghệ'),('b8751fb5-2ecd-472e-a7be-462e7d372096','Chuyển giao nghiên cứu'),('85882a05-f291-4d76-a079-76abeaa3a723','Chuyển giao nghiên cứu'),('40078327-5bc5-4960-877a-c81b9647417d','Chuyển giao nghiên cứu'),('dfd7f292-6a41-491b-bd6c-bee1c691fb87','Chuyển giao nghiên cứu'),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','Chuyển giao nghiên cứu'),('e9618cf7-f89f-45cc-890a-0bb07c837c5a','Chuyển giao công nghệ');
/*!40000 ALTER TABLE `topic_transfer_forms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user` varchar(255) NOT NULL,
  `role` int NOT NULL,
  PRIMARY KEY (`user`,`role`),
  KEY `FKkljow03640acn8axhon99sncn` (`role`),
  CONSTRAINT `FKerpv9jh7xrjmneq9x6h8gp1jp` FOREIGN KEY (`user`) REFERENCES `tbl_users` (`id`),
  CONSTRAINT `FKkljow03640acn8axhon99sncn` FOREIGN KEY (`role`) REFERENCES `tbl_roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('211115053120159',1),('1001',2),('1002',2),('18',2),('211115053120159',2),('21115053120126',2),('5',2),('thanhtuanle0209999',2),('1',3),('10',3),('11',3),('111',3),('12',3),('13',3),('14',3),('15',3),('16',3),('17',3),('18',3),('19',3),('2',3),('20',3),('21',3),('211115053120100',3),('211115053120134',3),('211115053120158',3),('21115053120106',3),('21115053120107',3),('21115053120108',3),('21115053120122',3),('21115053120124',3),('21115053120125',3),('21115053120126',3),('5',3),('6',3),('7',3),('8',3),('9',3),('doe',3),('sys_admin',3),('thanhtuanle',3),('pqlkhhtqt',5),('3',6),('4',6),('nguyentrangle2006',6),('thanhtnhan',6),('thanhtuanle0209',6),('thanhtuanle0209999',6),('1001',7),('1002',7),('17',7),('19',7),('2',7),('211115053120100',7),('211115053120134',7),('211115053120159',7),('21115053120122',7),('5',7),('doe',7);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-14 23:04:34
