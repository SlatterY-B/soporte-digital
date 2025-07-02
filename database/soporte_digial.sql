CREATE DATABASE  IF NOT EXISTS `soporte_digial` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `soporte_digial`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: soporte_digial
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activity_date` datetime DEFAULT NULL,
  `activity_description` varchar(255) DEFAULT NULL,
  `hours_worked` double DEFAULT NULL,
  `assignment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `assignment_id` (`assignment_id`),
  CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`assignment_id`) REFERENCES `assignment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'2025-05-25 18:40:00','the system logs were reviewed.',3.5,1),(2,'2025-05-26 15:40:00','the system logs see',4.5,5);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment`
--

DROP TABLE IF EXISTS `assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `support_request_id` bigint DEFAULT NULL,
  `agent_id` bigint DEFAULT NULL,
  `is_coordinator` tinyint(1) DEFAULT '0',
  `assigned_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `updated_by_user_id` bigint DEFAULT NULL,
  `assignment_status` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `support_request_id` (`support_request_id`),
  KEY `agent_id` (`agent_id`),
  KEY `updated_by_user_id` (`updated_by_user_id`),
  CONSTRAINT `assignment_ibfk_1` FOREIGN KEY (`support_request_id`) REFERENCES `support_request` (`id`),
  CONSTRAINT `assignment_ibfk_2` FOREIGN KEY (`agent_id`) REFERENCES `users` (`id`),
  CONSTRAINT `assignment_ibfk_3` FOREIGN KEY (`updated_by_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment`
--

LOCK TABLES `assignment` WRITE;
/*!40000 ALTER TABLE `assignment` DISABLE KEYS */;
INSERT INTO `assignment` VALUES (1,2,3,1,NULL,NULL,NULL,NULL),(2,3,11,1,'2025-05-25 21:30:16','2025-05-25 21:30:16',1,0),(3,5,13,0,'2025-05-25 22:08:55','2025-05-25 22:08:55',1,0),(4,7,11,1,'2025-05-25 22:19:35','2025-05-25 22:19:35',1,0),(5,8,12,1,'2025-05-25 22:24:47','2025-05-25 22:24:47',1,0),(6,2,13,1,'2025-05-25 22:33:31','2025-05-25 22:33:31',1,0),(7,4,3,0,'2025-05-25 23:07:01','2025-05-25 23:07:01',1,0),(8,6,11,0,'2025-05-25 23:23:06','2025-05-25 23:23:06',1,0);
/*!40000 ALTER TABLE `assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_request`
--

DROP TABLE IF EXISTS `support_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description_support` varchar(1000) DEFAULT NULL,
  `request_type` enum('BUG','TRAINING','REQUEST') DEFAULT NULL,
  `request_status` enum('PENDING','IN_PROGRESS','COMPLETED') DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `support_request_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_request`
--

LOCK TABLES `support_request` WRITE;
/*!40000 ALTER TABLE `support_request` DISABLE KEYS */;
INSERT INTO `support_request` VALUES (1,NULL,NULL,NULL,'PENDING','2025-05-25 15:32:11',2),(2,'Not found','it goes blank when entering','REQUEST','PENDING','2025-05-25 18:30:15',2),(3,'does not enter','I haven\'t been able to send any more messages.','BUG','PENDING','2025-05-25 18:37:47',4),(4,'Test','error sending data','TRAINING','PENDING','2025-05-25 18:44:30',6),(5,'Ready','dating for new','TRAINING','PENDING','2025-05-25 18:55:27',7),(6,'the system i slow','step out of nowhere','BUG','PENDING','2025-05-25 19:22:16',8),(7,'ask','the problem has not yet been fixed','TRAINING','PENDING','2025-05-25 19:33:23',9),(8,'testing','help with applications','REQUEST','PENDING','2025-05-25 19:39:02',10);
/*!40000 ALTER TABLE `support_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_user` varchar(255) DEFAULT NULL,
  `role_user` enum('CUSTOMER','AGENT','ADMIN') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Slatter Admin','flysoloxd@gmail.com','$2a$10$umX8qKH10VPyt5DFqI6gY.msbuznvprqOUhgSMNoSKvp5RtCnbO8q','ADMIN'),(2,'Thomy Perez','tortolito@gmail.com','$2a$10$bDEBY/lPxAjjJ7w0h8M3kutKH/4U41P8rv.WyhiyFrhzUGAIyegWi','CUSTOMER'),(3,'Remy Jons','agentRemy@gmail.com','$2a$10$iYLQY.kRjVy5Ogb5iRJ.terxob32qysa9Utk9Wj5NcZhKXSFCQ0DC','AGENT'),(4,'Timmy Lopez','loloyung@gmail.com','$2a$10$GEwHAgOsBV9ukKsex5NSU.D2XhWHetO492ZfqTNLKibz0uwTnn0bC','CUSTOMER'),(5,'Liam Ahr','musicg@gmail.com','$2a$10$uZYL/r4J4FdQESuzlL18YugNwjLNDtIznB2AA9huTeoFFZ2JJnlhG','CUSTOMER'),(6,'Rick Brad','trenalsurg@gmail.com','$2a$10$qZmzj.kQ5Uf6xzVBSnJNduM0uvkqJcBNvhxs9H/O90SrntQyCQaMK','CUSTOMER'),(7,'Charles lin','fastyou@gmail.com','$2a$10$fqregOE/g4qkPNds1Y/l4Oo0PjflPvYEDLssokvD6Mv0UDqqVyqQS','CUSTOMER'),(8,'Nill Yam','grantway@gmail.com','$2a$10$A4Loa/1XlhRNvOVoqNJX3.164wIiXPxFPIjGoMUplD66LDxm3dwo.','CUSTOMER'),(9,'Robert Flin','toyougoing@gmail.com','$2a$10$T45UKblqlJ643Rpbnm3l8.K5yXKS3K3cYiUMX9PWOD0ZsQYKmjEbO','CUSTOMER'),(10,'Sofia Prado','travelnext@gmail.com','$2a$10$IjFPCRLxLt2vLQd1bLoEveM0zW8d1Gy4pmhgMGHCCQ9Q8UL2ZLbk6','CUSTOMER'),(11,'Gisella Palomino','kiradone@gmail.com','$2a$10$AJDuJ/mnlQPQK6cIHe9jR..uSYnKrwmIWR4h1Ns4/8Fm1OPEZhy.m','AGENT'),(12,'Jony lara','pandas@gmail.com','$2a$10$v0FqWQImZHyEBfk2Q5mH0edgQ1dzfrey0c/DAsCuFU2MKY3paUdEi','AGENT'),(13,'Gustav Reyes','andesrun@gmail.com','$2a$10$sgFBuuu/7Dm59ujsv6YqyuA6f9eZt7fiiX.bvBE6NvSLCBgFex1sC','AGENT'),(14,'Hanna Rimen','hann21@gmail.com','$2a$10$0K.BvIEHM7Kau/rxdd.ZW.8FJxGEg1.c79jtFRut8N1dBxqDNXzci','CUSTOMER'),(15,'Elio Paucar','tikbrow@gmail.com','$2a$10$UX3eIUPePqihzYhPvPcpXOznLpuZSr4ew.Mee4V7R43400jEa3WWS','CUSTOMER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'soporte_digial'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-26 12:44:58
