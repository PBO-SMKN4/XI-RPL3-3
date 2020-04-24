-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 25, 2020 at 09:32 AM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `agenday`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id_absen` char(3) NOT NULL,
  `sakit` int(2) DEFAULT NULL,
  `izin` int(2) DEFAULT NULL,
  `alpha` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id_absen`, `sakit`, `izin`, `alpha`) VALUES
('668', 0, 0, 0),
('675', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `agenday`
--

CREATE TABLE `agenday` (
  `id_agenday` int(10) NOT NULL,
  `id_kelas` char(3) NOT NULL,
  `kd_mapel` char(10) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `cttn` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `agenday`
--

INSERT INTO `agenday` (`id_agenday`, `id_kelas`, `kd_mapel`, `tanggal`, `cttn`) VALUES
(1, '12', 'PBO', '2020-03-11', 'Mengerjakan Quiz DI Oracle Section 1-5');

-- --------------------------------------------------------

--
-- Stand-in structure for view `data_absen`
-- (See below for the actual view)
--
CREATE TABLE `data_absen` (
`nis` char(12)
,`nama` varchar(50)
,`jk` enum('L','P')
,`sakit` int(2)
,`izin` int(2)
,`alpha` int(2)
);

-- --------------------------------------------------------

--
-- Table structure for table `guru`
--

CREATE TABLE `guru` (
  `NIP` char(20) NOT NULL,
  `nama_guru` varchar(50) NOT NULL,
  `kd_mapel` char(10) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `tempat_lahir` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `guru`
--

INSERT INTO `guru` (`NIP`, `nama_guru`, `kd_mapel`, `tanggal_lahir`, `tempat_lahir`) VALUES
('-', 'ISNAINI MARIFATUN', 'PAI', '1980-01-05', 'BANDUNG'),
('1', 'FAJAR DEWI NINGSIH', 'BING', '1978-01-04', 'BOGOR'),
('197003132008011005', 'NANA SURYANA', 'OR', '1970-03-13', 'MAJALENGKA'),
('197409172005012010', 'ARNE HERLIANY', 'PKK', '1974-09-17', 'BANDUNG'),
('2', 'FERRY STEPHANUS SUWITA', 'PWPB', '1994-01-23', 'BANDUNG'),
('3', 'Hana Zainab Mukarromah', 'PWPB', '1992-10-03', 'MEKKAH'),
('4', 'Lukmannul Hakim Firdaus', 'PBO', '1993-01-06', 'BANDUNG'),
('5', 'MOH. ALI ALJAUHARI', 'BD', '1985-09-19', 'SINDANGKERTA'),
('6', 'OFFY SHOLEHATUN', 'PBO', '1979-12-11', 'KEBUMEN'),
('7', 'RINA PUJIATI', 'BD', '1986-10-29', 'BANDUNG'),
('8', 'SUSTENI MARTIANI', 'MAT', '1990-03-14', 'GARUT'),
('9', 'YUYUN TRESNAWATI', 'PPL', '1973-10-09', 'BANDUNG');

-- --------------------------------------------------------

--
-- Table structure for table `kelas`
--

CREATE TABLE `kelas` (
  `id_kelas` char(3) NOT NULL,
  `nama_kelas` char(10) NOT NULL,
  `jurusan` varchar(35) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kelas`
--

INSERT INTO `kelas` (`id_kelas`, `nama_kelas`, `jurusan`) VALUES
('1', 'X AVI 1', 'AUDIO VIDEO'),
('10', 'X TKJ 2', 'TEKNIK KOMPUTER JARINGAN'),
('11', 'X RPL 1', 'REKAYASA PERANGKAT LUNAK'),
('12', 'X RPL 2', 'REKAYASA PERANGKAT LUNAK'),
('13', 'X RPL 3', 'REKAYASA PERANGKAT LUNAK'),
('14', 'X MM', 'MULTI MEDIA'),
('15', 'XI AVI 1', 'AUDIO VIDEO'),
('16', 'XI AVI 2', 'AUDIO VIDEO'),
('17', 'XI AVI 3', 'AUDIO VIDEO'),
('18', 'XI TITL 1', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('19', 'XI TITL 2', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('2', 'X AVI 2', 'AUDIO VIDEO'),
('20', 'XI TOI 1', 'TEKNIK OTOMASI INDUSTRI'),
('21', 'XI TOI 2', 'TEKNIK OTOMASI INDUSTRI'),
('22', 'XI TKJ 1', 'TEKNIK KOMPUTER JARINGAN'),
('23', 'XI TKJ 2', 'TEKNIK KOMPUTER JARINGAN'),
('24', 'XI RPL 1', 'REKAYASA PERANGKAT LUNAK'),
('25', 'XI RPL 2', 'REKAYASA PERANGKAT LUNAK'),
('26', 'XI RPL 3', 'REKAYASA PERANGKAT LUNAK'),
('27', 'XI MM', 'MULTI MEDIA'),
('28', 'XII AVI 1', 'AUDIO VIDEO'),
('29', 'XII AVI 2', 'AUDIO VIDEO'),
('3', 'X AVI 3', 'AUDIO VIDEO'),
('30', 'XII AVI 1', 'AUDIO VIDEO'),
('31', 'XII AVI 2', 'AUDIO VIDEO'),
('32', 'XII TITL 2', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('33', 'XII TITL 1', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('34', 'XII TOI 1', 'TEKNIK OTOMASI INDUSTRI'),
('35', 'XII TOI 2', 'TEKNIK OTOMASI INDUSTRI'),
('36', 'XII TKJ 1', 'TEKNIK KOMPUTER JARINGAN'),
('37', 'XII RPL 1', 'REKAYASA PERANGKAT LUNAK'),
('38', 'XII RPL 2', 'REKAYASA PERANGKAT LUNAK'),
('39', 'XII RPL 3', 'REKAYASA PERANGKAT LUNAK'),
('4', 'X AVI 4', 'AUDIO VIDEO'),
('40', 'XII MM', 'MULTI MEDIA'),
('5', 'X TITL 1', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('6', 'X TITL 2', 'TEKNIK INSTALASI TENAGA LISTRIK'),
('7', 'X TOI 1', 'TEKNIK OTOMASI INDUSTRI'),
('8', 'X TOI 2', 'TEKNIK OTOMASI INDUSTRI'),
('9', 'X TKJ 1', 'TEKNIK KOMPUTER JARINGAN');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `username` varchar(24) DEFAULT NULL,
  `paswword` varchar(24) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`username`, `paswword`) VALUES
('197003132008011005', '197003132008011005'),
('197409172005012010', '197409172005012010');

-- --------------------------------------------------------

--
-- Table structure for table `mapel`
--

CREATE TABLE `mapel` (
  `kd_mapel` char(10) NOT NULL,
  `nama_mapel` varchar(50) NOT NULL,
  `waktu` char(10) NOT NULL,
  `total_jam` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mapel`
--

INSERT INTO `mapel` (`kd_mapel`, `nama_mapel`, `waktu`, `total_jam`) VALUES
('BD', 'Basis Data', '180 menit', 4),
('BIND', 'Bahasa INDONESIA', '135 menit', 3),
('BING', 'Bahasa Inggris', '135 menit', 3),
('BK', 'Bimbingan Konseling', '90 menit', 2),
('BS', 'Bahasa Sunda', '90 menit', 2),
('Mat', 'Matematika', '180 menit', 4),
('OR', 'Olahraga', '90 menit', 2),
('PAI', 'Pendidikan Agama Islam', '135 menit', 3),
('PBO', 'Pemrograman BerOrientasi Objek', '360 menit', 8),
('PKK', 'Produktif kreatif dan Kewirausahaan', '315 menit', 7),
('PKN', 'Pendidikan Panca Sila Dan KewargaNegaraan', '90 menit', 2),
('PPL', 'Pemodelan Perangkat Lunak', '180 menit', 4),
('PWPB', 'Pemrograman Web Dan Perangkat Bergerak', '360 menit', 8);

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `nis` char(12) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `jk` enum('L','P') DEFAULT NULL,
  `tempat_lahir` varchar(50) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `id_kelas` char(3) DEFAULT NULL,
  `id_absen` char(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`nis`, `nama`, `jk`, `tempat_lahir`, `tanggal_lahir`, `id_kelas`, `id_absen`) VALUES
('1819117668', 'RAMDAN ROHENDI', 'L', 'BANDUNG', '2002-11-19', '12', '668'),
('1819117675', 'ZAHY HABIBI', 'L', 'BANDUNG', '2003-05-19', '12', '675');

-- --------------------------------------------------------

--
-- Structure for view `data_absen`
--
DROP TABLE IF EXISTS `data_absen`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `data_absen`  AS  select `siswa`.`nis` AS `nis`,`siswa`.`nama` AS `nama`,`siswa`.`jk` AS `jk`,`absensi`.`sakit` AS `sakit`,`absensi`.`izin` AS `izin`,`absensi`.`alpha` AS `alpha` from (`siswa` join `absensi`) where `siswa`.`id_absen` = `absensi`.`id_absen` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id_absen`);

--
-- Indexes for table `agenday`
--
ALTER TABLE `agenday`
  ADD PRIMARY KEY (`id_agenday`),
  ADD KEY `id_kelas` (`id_kelas`),
  ADD KEY `kd_mapel` (`kd_mapel`);

--
-- Indexes for table `guru`
--
ALTER TABLE `guru`
  ADD PRIMARY KEY (`NIP`),
  ADD KEY `kd_mapel` (`kd_mapel`);

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`id_kelas`);

--
-- Indexes for table `mapel`
--
ALTER TABLE `mapel`
  ADD PRIMARY KEY (`kd_mapel`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`nis`),
  ADD KEY `id_kelas` (`id_kelas`),
  ADD KEY `id_absen` (`id_absen`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agenday`
--
ALTER TABLE `agenday`
  ADD CONSTRAINT `agenday_ibfk_1` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`),
  ADD CONSTRAINT `agenday_ibfk_2` FOREIGN KEY (`kd_mapel`) REFERENCES `mapel` (`kd_mapel`);

--
-- Constraints for table `guru`
--
ALTER TABLE `guru`
  ADD CONSTRAINT `guru_ibfk_1` FOREIGN KEY (`kd_mapel`) REFERENCES `mapel` (`kd_mapel`);

--
-- Constraints for table `siswa`
--
ALTER TABLE `siswa`
  ADD CONSTRAINT `siswa_ibfk_1` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`),
  ADD CONSTRAINT `siswa_ibfk_2` FOREIGN KEY (`id_absen`) REFERENCES `absensi` (`id_absen`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
