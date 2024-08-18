-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: products_db
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `barcode` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `barcode` (`barcode`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'9164035109868','Essence Mascara Lash Princess','https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/1.png',4.94,9.99),(2,'2817839095220','Eyeshadow Palette with Mirror','https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/1.png',3.28,19.99),(3,'0516267971277','Powder Canister','https://cdn.dummyjson.com/products/images/beauty/Powder%20Canister/1.png',3.82,14.99),(4,'9444582199406','Red Lipstick','https://cdn.dummyjson.com/products/images/beauty/Red%20Lipstick/1.png',2.51,12.99),(5,'3212847902461','Red Nail Polish','https://cdn.dummyjson.com/products/images/beauty/Red%20Nail%20Polish/1.png',3.91,8.99),(6,'2210136215089','Calvin Klein CK One','https://cdn.dummyjson.com/products/images/fragrances/Calvin%20Klein%20CK%20One/1.png',4.85,49.99),(7,'1435582999795','Chanel Coco Noir Eau De','https://cdn.dummyjson.com/products/images/fragrances/Chanel%20Coco%20Noir%20Eau%20De/1.png',2.76,129.99),(8,'0887083199279','Dior J\'adore','https://cdn.dummyjson.com/products/images/fragrances/Dior%20J\'adore/1.png',3.31,89.99),(9,'1939383392674','Dolce Shine Eau de','https://cdn.dummyjson.com/products/images/fragrances/Dolce%20Shine%20Eau%20de/1.png',2.68,69.99),(10,'8232190382069','Gucci Bloom Eau de','https://cdn.dummyjson.com/products/images/fragrances/Gucci%20Bloom%20Eau%20de/1.png',2.69,79.99),(11,'7113807059215','Annibale Colombo Bed','https://cdn.dummyjson.com/products/images/furniture/Annibale%20Colombo%20Bed/1.png',4.14,1899.99),(12,'0426785817074','Annibale Colombo Sofa','https://cdn.dummyjson.com/products/images/furniture/Annibale%20Colombo%20Sofa/1.png',3.08,2499.99),(13,'2913244159666','Bedside Table African Cherry','https://cdn.dummyjson.com/products/images/furniture/Bedside%20Table%20African%20Cherry/1.png',4.48,299.99),(14,'0726316339746','Knoll Saarinen Executive Conference Chair','https://cdn.dummyjson.com/products/images/furniture/Knoll%20Saarinen%20Executive%20Conference%20Chair/1.png',4.11,499.99),(15,'7839797529453','Wooden Bathroom Sink With Mirror','https://cdn.dummyjson.com/products/images/furniture/Wooden%20Bathroom%20Sink%20With%20Mirror/1.png',3.26,799.99),(16,'2517819903837','Apple','https://cdn.dummyjson.com/products/images/groceries/Apple/1.png',2.96,1.99),(17,'8335515097879','Beef Steak','https://cdn.dummyjson.com/products/images/groceries/Beef%20Steak/1.png',2.83,12.99),(18,'5503491330693','Cat Food','https://cdn.dummyjson.com/products/images/groceries/Cat%20Food/1.png',2.88,8.99),(19,'0966223559510','Chicken Meat','https://cdn.dummyjson.com/products/images/groceries/Chicken%20Meat/1.png',4.61,9.99),(20,'6707669443381','Cooking Oil','https://cdn.dummyjson.com/products/images/groceries/Cooking%20Oil/1.png',4.01,4.99),(21,'2597004869708','Cucumber','https://cdn.dummyjson.com/products/images/groceries/Cucumber/1.png',4.71,1.49),(22,'7957222289508','Dog Food','https://cdn.dummyjson.com/products/images/groceries/Dog%20Food/1.png',2.74,10.99),(23,'7095702028776','Eggs','https://cdn.dummyjson.com/products/images/groceries/Eggs/1.png',4.46,2.99),(24,'4250692197342','Fish Steak','https://cdn.dummyjson.com/products/images/groceries/Fish%20Steak/1.png',4.83,14.99),(25,'7583442707568','Green Bell Pepper','https://cdn.dummyjson.com/products/images/groceries/Green%20Bell%20Pepper/1.png',4.28,1.29),(26,'8400326844874','Green Chili Pepper','https://cdn.dummyjson.com/products/images/groceries/Green%20Chili%20Pepper/1.png',4.43,0.99),(27,'0668665656044','Honey Jar','https://cdn.dummyjson.com/products/images/groceries/Honey%20Jar/1.png',3.5,6.99),(28,'9603960319256','Ice Cream','https://cdn.dummyjson.com/products/images/groceries/Ice%20Cream/1.png',3.77,5.49),(29,'8546824122355','Juice','https://cdn.dummyjson.com/products/images/groceries/Juice/1.png',3.41,3.99),(30,'3325493172934','Kiwi','https://cdn.dummyjson.com/products/images/groceries/Kiwi/1.png',4.37,2.49);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-22 16:53:32
