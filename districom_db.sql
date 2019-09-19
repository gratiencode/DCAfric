-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 28 Août 2019 à 13:23
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `districom_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `agents`
--

CREATE TABLE IF NOT EXISTS `agents` (
  `ID` varchar(255) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `agent_password` text,
  `_bloquee` tinyint(1) DEFAULT '0',
  `date_naisse` date DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `postnom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `role_agent` text,
  `sexe` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE IF NOT EXISTS `client` (
  `ID` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE IF NOT EXISTS `commande` (
  `comment` longtext,
  `_date_` datetime DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `nombre_article` int(11) DEFAULT NULL,
  `valide` tinyint(1) DEFAULT '0',
  `methode` varchar(20) NOT NULL DEFAULT 'CASH',
  `reference` varchar(255) NOT NULL,
  `id_client` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `id_kiosque` varchar(255) NOT NULL,
  PRIMARY KEY (`reference`,`id_client`,`ID`,`id_kiosque`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `consommer`
--

CREATE TABLE IF NOT EXISTS `consommer` (
  `autre_info` varchar(255) DEFAULT NULL,
  `_date` datetime DEFAULT NULL,
  `devise` varchar(255) DEFAULT NULL,
  `etat` varchar(255) DEFAULT NULL,
  `id_kiosq` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `montant` double DEFAULT NULL,
  `quantite` double DEFAULT NULL,
  `methode` varchar(20) NOT NULL DEFAULT 'CASH',
  `valide` tinyint(1) DEFAULT '0',
  `id_client` varchar(255) NOT NULL,
  `id_service` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  PRIMARY KEY (`id_client`,`id_service`,`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `kiosque`
--

CREATE TABLE IF NOT EXISTS `kiosque` (
  `ID` varchar(255) NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `materiel`
--

CREATE TABLE IF NOT EXISTS `materiel` (
  `ID` varchar(255) NOT NULL,
  `fabriquant` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `nom_materiel` varchar(255) DEFAULT NULL,
  `quantite_materiel` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `obtenir`
--

CREATE TABLE IF NOT EXISTS `obtenir` (
  `_date` date DEFAULT NULL,
  `id_kiosq` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `id_agent` varchar(255) NOT NULL,
  PRIMARY KEY (`id_kiosq`,`ID`,`id_agent`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `parametre`
--

CREATE TABLE IF NOT EXISTS `parametre` (
  `clef` varchar(255) NOT NULL,
  `valeur` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`clef`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `posseder`
--

CREATE TABLE IF NOT EXISTS `posseder` (
  `autre_info` varchar(255) DEFAULT NULL,
  `_date` date DEFAULT NULL,
  `id_materiel` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `id_kiosque` varchar(255) NOT NULL,
  PRIMARY KEY (`id_materiel`,`ID`,`id_kiosque`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE IF NOT EXISTS `produit` (
  `ID` varchar(255) NOT NULL,
  `fournisseur` varchar(255) DEFAULT NULL,
  `nom_produit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `recquisitionner`
--

CREATE TABLE IF NOT EXISTS `recquisitionner` (
  `commentaire` varchar(255) DEFAULT NULL,
  `_date` datetime DEFAULT NULL,
  `devise` varchar(255) DEFAULT NULL,
  `prix_unit` double DEFAULT NULL,
  `quant` double DEFAULT NULL,
  `id_produit` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `id_kiosque` varchar(255) NOT NULL,
  PRIMARY KEY (`id_produit`,`ID`,`id_kiosque`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `service`
--

CREATE TABLE IF NOT EXISTS `service` (
  `ID` varchar(255) NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  `nom_service` varchar(255) DEFAULT NULL,
  `unit_mesure` varchar(255) DEFAULT NULL,
  `tarif_cash` double NOT NULL,
  `devise_cash` varchar(5) NOT NULL DEFAULT 'CDF',
  `tarif_mobmoney` double NOT NULL,
  `devise_mobmoney` varchar(5) NOT NULL DEFAULT 'CDF',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE IF NOT EXISTS `vente` (
  `_date` datetime DEFAULT NULL,
  `devise` varchar(255) DEFAULT NULL,
  `mantant` double DEFAULT NULL,
  `quantite` double DEFAULT NULL,
  `reference` varchar(255) NOT NULL,
  `id_produit` varchar(255) NOT NULL,
  `ID` int(11) NOT NULL,
  PRIMARY KEY (`reference`,`id_produit`,`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
