create database QLCHBS -- Chạy dầu

--=============================CHẠY TỪ ĐÂY========================================
use QLCHBS

-- Tạo bảng KhachHang
CREATE TABLE KhachHang (
    MaKH CHAR(5),
    TenKH NVARCHAR(50),
    DiaChi NVARCHAR(100),
    SDT CHAR(10),
    Email VARCHAR(50),
	CONSTRAINT PK_KhachHang PRIMARY KEY (MaKH)
)

-- Tạo bảng NhanVien
CREATE TABLE NhanVien (
    MaNV CHAR(5),
    TenNV NVARCHAR(50),
	ChucVu NVARCHAR(50),
    DiaChi NVARCHAR(100),
    SDT CHAR(10),
	CONSTRAINT PK_NhanVien PRIMARY KEY (MaNV)
)

-- Tạo bảng TacGia
CREATE TABLE TacGia (
    MaTG CHAR(5),
    TenTG NVARCHAR(100),
	CONSTRAINT PK_TacGia PRIMARY KEY (MaTG)
)

-- Tạo bảng DanhMucSach
CREATE TABLE DanhMucSach (
    MaDanhMuc CHAR(5),
    TenDanhMuc NVARCHAR(100),
	CONSTRAINT PK_DanhMucSach PRIMARY KEY (MaDanhMuc)
)

-- Tạo bảng Sach
CREATE TABLE Sach (
    MaSach CHAR(5),
    MaTG CHAR(5),
    MaDanhMuc CHAR(5),
    GiaBan DECIMAL(10,2),
    SLKho INT,
    TenSach NVARCHAR(100),
	CONSTRAINT PK_Sach PRIMARY KEY (MaSach)
)

-- Tạo bảng DonHang
CREATE TABLE DonHang (
    MaDH CHAR(5),
    MaKH CHAR(5),
	MaNV CHAR(5),
    NgayDat DATE,
    TongTien DECIMAL(10,2),
	TrangThai BIT DEFAULT 0,
	CONSTRAINT PK_DonHang PRIMARY KEY (MaDH)
)

-- Tạo bảng ChiTietDonHang
CREATE TABLE ChiTietDonHang (
	MaDH CHAR(5),
    MaSach CHAR(5),
    SoLuong INT,
    GiaBan DECIMAL(10,2),
	CONSTRAINT PK_ChiTietDonHang PRIMARY KEY (MaDH, MaSach)
)

-- Tạo bảng NhaCungCap
CREATE TABLE NhaCungCap(
	MaNCC CHAR(5),
	TenNCC NVARCHAR(100),
	DiaChi NVARCHAR(100),
	SDT CHAR(11),
	Email VARCHAR(50),
	CONSTRAINT PK_NhaCungCap PRIMARY KEY (MaNCC)
)

-- Tạo bảng PhieuNhap
CREATE TABLE PhieuNhap (
	MaPhieu CHAR(5),
	MaNCC CHAR(5),
	MaNV CHAR(5),
	NgayNhap DATE,
	TongTien DECIMAL(10,2),
	CONSTRAINT PK_PhieuNhap PRIMARY KEY (MaPhieu)
)

-- Tạo bảng ChiTietPhieuNhap
CREATE TABLE ChiTietPhieuNhap (
	MaPhieu CHAR(5),
	MaSach CHAR(5),
	SoLuong INT,
	GiaNhap DECIMAL(10,2)
	CONSTRAINT PK_ChiTietPhieuNhap PRIMARY KEY (MaPhieu, MaSach)
)

-- Khóa ngoại
alter table Sach
add constraint FK_Sach_TacGia foreign key (MaTG) references TacGia(MaTG),
constraint FK_Sach_DanhMucSach foreign key (MaDanhMuc) references DanhMucSach(MaDanhMuc)

alter table DonHang
add constraint FK_DonHang_KhachHang foreign key (MaKH) references KhachHang(MaKH),
constraint FK_DonHang_NhanVien foreign key (MaNV) references NhanVien(MaNV)

alter table ChiTietDonHang
add constraint FK_ChiTietDonHang_Sach foreign key (MaSach) references Sach(MaSach),
constraint FK_ChiTietDonHang_DonHang foreign key (MaDH) references DonHang(MaDH)

alter table PhieuNhap
add constraint FK_PhieuNhap_NhaCungCap foreign key (MaNCC) references NhaCungCap(MaNCC),
constraint FK_PhieuNhap_NhanVien foreign key (MaNV) references NhanVien(MaNV)

alter table ChiTietPhieuNhap
add constraint FK_ChiTietPhieuNhap_PhieuNhap foreign key (MaPhieu) references PhieuNhap(MaPhieu),
constraint FK_ChiTietPhieuNhap_Sach foreign key (MaSach) references Sach(MaSach)

-- Các ràng buộc
alter table TacGia
add constraint UNI_TenTG unique (TenTG)

alter table DanhMucSach
add constraint UNI_TenDanhMuc unique (TenDanhMuc)

alter table Sach
add constraint CK_GiaBan check (GiaBan > 0),
constraint UNI_TenSach unique (TenSach),
constraint CK_SLKho check (SLKho >= 0)

alter table ChiTietDonHang
add constraint Check_GiaBan check (GiaBan > 0),
constraint Check_SL check (SoLuong > 0)

alter table ChiTietPhieuNhap
add constraint Check_GiaNhap check (GiaNhap > 0),
constraint Check_SLNhap check (SoLuong > 0)

-----------------------------------------------------TRIGGER------------------------------------------------------------
--trigger sau khi chèn (INSERT) hoặc cập nhật (UPDATE) vào ChiTietDonHang
GO
CREATE TRIGGER Trig_TongTien_Update
ON ChiTietDonHang
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @MaDH CHAR(5);

    SELECT @MaDH = i.MaDH
    FROM inserted i;

    UPDATE DonHang
    SET TongTien = (
        SELECT SUM(GiaBan * SoLuong)
        FROM ChiTietDonHang
        WHERE MaDH = @MaDH
        GROUP BY MaDH -- Thêm GROUP BY ở đây để nhóm theo MaDH
    )
    WHERE MaDH = @MaDH;
END;
GO

-- Tính tổng tiền phiếu nhập
GO
CREATE TRIGGER Trigger_TongTienNhap
ON ChiTietPhieuNhap
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    IF EXISTS (SELECT 1 FROM inserted) 
    BEGIN
        UPDATE PN
        SET TongTien = (
            SELECT SUM(ct.GiaNhap * ct.SoLuong)
            FROM ChiTietPhieuNhap ct
            WHERE ct.MaPhieu = PN.MaPhieu
        )
        FROM PhieuNhap PN
        INNER JOIN inserted i ON PN.MaPhieu = i.MaPhieu
    END
    ELSE
    BEGIN
        UPDATE PN
        SET TongTien = (
            SELECT SUM(ct.GiaNhap * ct.SoLuong)
            FROM ChiTietPhieuNhap ct
            WHERE ct.MaPhieu = PN.MaPhieu
        )
        FROM PhieuNhap PN
        INNER JOIN deleted d ON PN.MaPhieu = d.MaPhieu
    END
END

GO
CREATE TRIGGER TR_CapNhatTonKho
ON ChiTietPhieuNhap
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    IF EXISTS (SELECT 1 FROM inserted) 
    BEGIN
        -- Xử lý khi thêm mới hoặc cập nhật
        UPDATE Sach
        SET SLKho = SLKho + (i.SoLuong - COALESCE(d.SoLuong, 0))
        FROM Sach s
        INNER JOIN inserted i ON s.MaSach = i.MaSach
        LEFT JOIN deleted d ON i.MaSach = d.MaSach AND i.SoLuong <> d.SoLuong
    END
    ELSE
    BEGIN
        -- Xử lý khi xóa
        UPDATE Sach
        SET SLKho = SLKho - d.SoLuong
        FROM Sach s
        INNER JOIN deleted d ON s.MaSach = d.MaSach
    END
END
GO

----trigger thêm và cập nhật số lượng ở chi tiết đơn hàng với SLKho
CREATE OR ALTER TRIGGER Trig_GiamTonKho_Update
ON ChiTietDonHang
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaSach NVARCHAR(50), @SoLuong INT;

    -- Inserted or Updated
    SELECT @MaSach = MaSach, @SoLuong = SoLuong FROM inserted;

    -- Check if SoLuong is greater than SLKho
    IF EXISTS (SELECT 1 FROM Sach WHERE MaSach = @MaSach AND @SoLuong > SLKho)
    BEGIN
        RAISERROR ('Số lượng không được lớn hơn số lượng trong kho.', 16, 1);
        ROLLBACK;
        RETURN;
    END

    -- Update SLKho in Sach table
    UPDATE Sach
    SET SLKho = SLKho - @SoLuong
    WHERE MaSach = @MaSach;
END;
go

CREATE TRIGGER Trig_UpdateTongTien_AfterDelete
ON ChiTietDonHang
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaDH CHAR(5);

    -- Lấy MaDH từ bảng deleted
    SELECT @MaDH = d.MaDH
    FROM deleted d;

    -- Cập nhật lại TongTien trong bảng DonHang
    UPDATE DonHang
    SET TongTien = (
        SELECT ISNULL(SUM(SoLuong * GiaBan), 0)
        FROM ChiTietDonHang
        WHERE MaDH = @MaDH
    )
    WHERE MaDH = @MaDH;
END;
GO

CREATE TRIGGER Trig_XoaChiTietDonHang_UpdateSLKho
ON ChiTietDonHang
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaSach CHAR(5), @SoLuong INT;

    -- Lấy thông tin từ bản ghi đã xóa
    SELECT @MaSach = d.MaSach, @SoLuong = d.SoLuong
    FROM deleted d;

    -- Cập nhật lại SLKho trong bảng Sach
    UPDATE Sach
    SET SLKho = SLKho + @SoLuong
    WHERE MaSach = @MaSach;
END;
GO

-- Thêm dữ liệu vào bảng
insert into KhachHang values
('KH001', N'Nguyễn Văn Hòa', N'140 Tây Thạnh, Tân Phú, TPHCM', '0941824972', 'nguyenhoa@gmail.com'),
('KH002', N'Trần Hoàng Thịnh', N'121 Nguyễn Quý Anh, Tân Sơn Nhì, TPHCM', '0837528458', 'tranthinh@gmail.com'),
('KH003', N'Trần Thu Hằng', N'66 Đường số 11, Bình Hưng, Bình Chánh, TPHCM', '0736537149', 'thutran@gmail.com'),
('KH004', N'Võ Thùy Trang', N'986 Tạ Quang Bửu, Phường 6, Quận 8, TPHCM', '0283941023', 'trangvo@gmail.com'),
('KH005', N'Hồ Văn Trung', N'66/34 Phổ Quang, Phường 2, Tân Bình, TPHCM', '0637459206', 'trungho@gmail.com'),
('KH006', N'Nguyễn Thị Thùy My', N'7/6 Bùi Thị Xuân, Phường 2, Tân Bình, TPHCM', '0372857495', 'mynguyen@gmail.com')

insert into NhanVien values
('NV001', N'Trần Nguyệt Nga', N'Quản lý', N'66 Đường số 11, Bình Hưng, Bình Chánh, TPHCM', '0583948294'),
('NV002', N'Nguyễn Văn Thái', N'Nhân viên', N'32 Tây Thạnh, Tân Phú, TPHCM', '0746582014'),
('NV003', N'Trần Nguyễn Thịnh', N'Nhân viên', N'121 Nguyễn Quý Anh, Tân Sơn Nhì, TPHCM', '0756438592')

insert into TacGia values
('TG001', N'Trương Học Vĩ'),
('TG002', N'Carol S. Dweck'),
('TG003', N'Thích Nhất Hạnh'),
('TG004', N'Phùng Quán'),
('TG005', N'Hae Min'),
('TG006', N'Koga Fumitake, Kishimi Ichiro'),
('TG007', N'John Perkins'),
('TG008', N'Cal Newport'),
('TG009', N'Nhiều Tác Giả'),
('TG010', N'Raymond Murphy')

insert into DanhMucSach values
('KNN', N'Kĩ năng sống'),
('VH', N'Văn học'),
('KT', N'Kinh tế'),
('NN', N'Ngoại ngữ')

insert into Sach values
('SH001', 'TG001', 'KNN', 92.400, 60, N'Ổn Định Hay Tự Do'),
('SH002', 'TG002', 'KNN', 184.000, 50, N'Tâm Lý Học Thành Công'),
('SH003', 'TG003', 'KNN', 74.000, 10, N'Không Diệt Không Sinh Đừng Sợ Hãi'),
('SH004', 'TG004', 'VH', 59.500, 70, N'Tuổi Thơ Dữ Dội - Tập 1'),
('SH005', 'TG005', 'VH', 104.300, 25, N'Yêu Những Điều Không Hoàn Hảo'),
('SH006', 'TG007', 'KT', 143.000, 30, N'Lời Thú Tội Mới Của Một Sát Thủ Kinh Tế'),
('SH007', 'TG008', 'KT', 110.000, 17, N'Làm Ra Làm, Chơi Ra Chơi'),
('SH008', 'TG006', 'VH', 67.000, 5, N'Dám Bị Ghét'),
('SH009', 'TG009', 'NN', 74.000, 25, N'i-Learn Smart Start Grade 5 Workbook'),
('SH010', 'TG010', 'NN', 163.800, 12, N'English Grammar in Use Book w Ans')

insert into DonHang values
('DH001', 'KH001', 'NV001', '2023-10-16', 92.400, 1),
('DH002', 'KH002', 'NV001', '2023-11-25', 74.000, 1),
('DH003', 'KH003', 'NV003', '2023-10-13', 286.000, 1),
('DH004', 'KH004', 'NV002', '2023-11-23', 418.000, 1),
('DH005', 'KH005', 'NV003', '2023-10-11', 201.000, 1),
('DH006', 'KH006', 'NV001', '2023-10-10', 367.300, 1),
('DH007', 'KH003', 'NV002', '2023-10-19', 59.500, 0),
('DH008', 'KH005', 'NV002', '2023-11-29', 214.300, 0)

insert into ChiTietDonHang values
('DH001', 'SH001', 1, 92.400),
('DH002', 'SH003', 1, 74.000),
('DH003', 'SH006', 2, 143.000),
('DH004', 'SH001', 1, 92.400),
('DH004', 'SH010', 2, 163.800),
('DH005', 'SH008', 3, 67.000),
('DH006', 'SH002', 2, 183.650),
('DH007', 'SH004', 1, 59.500),
('DH008', 'SH005', 1, 104.300),
('DH008', 'SH007', 1, 110.000)

insert into NhaCungCap values -- 2 số của ncc đầu là sđt bàn nên có 11 số
('NCC01', N'Công Ty Cổ Phần Phát Hành Sách Tp.HCM', N'60-62 Lê Lợi, Bến Nghé, Quận 1, Tp.HCM', '02838225798', 'fahasa-sg@hcm.vnn.vn'),
('NCC02', N'Công Ty TNHH Văn Hóa Việt Long', N'14/35, Đào Duy Anh, P.9, Q.Phú Nhuận, Tp.HCM', '02838452708', 'info@vietlonbook.com'),
('NCC03', N'CÔNG TY TNHH MTV AN LỘC VIỆT', N'30 Kha Vạn Cân,Hiệp Bình Chánh,Thủ Đức,Tp.HCM', '0899189499', 'vppanlocviet@gmail.com')

insert into PhieuNhap values
('PN001', 'NCC01', 'NV001', '2023-09-15', null),
('PN002', 'NCC02', 'NV001', '2023-09-15', null),
('PN003', 'NCC03', 'NV002', '2023-10-01', null)

insert into ChiTietPhieuNhap values
('PN001', 'SH001', 20, 91.000),
('PN001', 'SH002', 20, 183.000),
('PN001', 'SH003', 20, 73.000),
('PN001', 'SH004', 20, 59.000),
('PN002', 'SH005', 20, 103.000),
('PN002', 'SH006', 20, 142.000),
('PN002', 'SH007', 20, 109.000),
('PN003', 'SH008', 20, 66.000),
('PN003', 'SH009', 20, 73.000),
('PN003', 'SH010', 20, 162.000)

-----------------------------------------------------THỐNG KÊ------------------------------------------------------------
--=============================Thống kê doanh thu theo tháng============================================
GO
create function thongKeTheoThang (@thang int)
returns decimal(10, 2)
as
begin
	declare @tt decimal(10, 2)

	select @tt = SUM(TongTien)
	from DonHang
	where MONTH(NgayDat) = @thang

	if @tt is null
		set @tt = 0

	return @tt
end
--=============================Thống kê doanh thu theo năm============================================
GO
create function thongKeTheoNam (@nam int)
returns decimal(10, 2)
as
begin
	declare @tt decimal(10, 2)

	select @tt = SUM(TongTien)
	from DonHang
	where YEAR(NgayDat) = @nam

	if @tt is null
		set @tt = 0

	return @tt
end
GO
--=============================Thống kê sách bán chạy( số lượng bán trên 50)============================================
GO
create function thongKeSachBanChay ()
returns @dss table(MaSach CHAR(5), TenSach NVARCHAR(200), TenTG NVARCHAR(100), GiaBan DECIMAL(10,2), SLKho INT)
as
begin
	insert into @dss 
		select s.MaSach, TenSach, TenTG, s.GiaBan, SLKho
		from Sach s 
		join TacGia tg on s.MaTG = tg.MaTG
		join ChiTietDonHang ct on s.MaSach = ct.MaSach
		group by s.MaSach, TenSach, TenTG, s.GiaBan, SLKho
		having SUM(SoLuong) > 50

	return
end
GO

insert into ChiTietDonHang values
('DH005', 'SH001', 55, 67.000),
('DH006', 'SH004', 60, 183.650)

select * from thongKeSachBanChay()
--=============================Thống kê khách hàng mua nhiều( tiền mua trên 500.000)============================================
GO
create function thongKeKhachMuaNhieu ()
returns @dsk table(MaKH CHAR(5), TenKH NVARCHAR(50), SDT VARCHAR(10), TongTien DECIMAL(10,2))
as
begin
	insert into @dsk 
		select kh.MaKH, TenKH, SDT, SUM(TongTien)
		from KhachHang kh
		join DonHang dh on kh.MaKH = dh.MaKH
		GROUP BY kh.MaKH, TenKH, SDT
		HAVING SUM(TongTien) > 500.000

	return
end
GO

insert into DonHang values
('DH009','KH003', 'NV003', '2023-10-16', 400.000, 1),
('DH010','KH001', 'NV003','2023-10-16', 500.000, 1)

select * from thongKeKhachMuaNhieu()

GO
CREATE OR ALTER PROC themDonHang 
    @maDH char(5),
    @maKH char(5), 
    @maNV char(5), 
	@thanhtien float,
    @trangthai bit = 0  -- Sử dụng kiểu dữ liệu bit cho trạng thái và đặt giá trị mặc định là 0
AS 
BEGIN
    INSERT INTO DonHang(MaDH, MaKH, MaNV, NgayDat,TongTien, TrangThai) 
    VALUES (@maDH, @maKH, @maNV, GETDATE(),@thanhtien, @trangthai)
END
GO

--4.PROCEDUCE THÊM CHI TIẾT ĐƠN HÀNG
CREATE PROCEDURE themChiTietDonHang @maDH CHAR(5), @maSach CHAR(5), @soLuong INT,@giaBan DECIMAL(10,2)
AS 
BEGIN
    INSERT INTO ChiTietDonHang (MaDH, MaSach, SoLuong, GiaBan) VALUES (@maDH, @maSach, @soLuong, @giaBan)

    UPDATE DonHang
    SET TongTien = (SELECT ISNULL(SUM(SoLuong * GiaBan), 0) 
                   FROM ChiTietDonHang 
                   WHERE MaDH = @maDH)
    WHERE MaDH = @maDH
END
GO

---thêm phần khi thêm sửa chi tiết đơn hàng
CREATE PROCEDURE capNhatTongTienDonHang
    @maDH CHAR(5)
AS 
BEGIN
    UPDATE DonHang
    SET TongTien = (SELECT ISNULL(SUM(SoLuong * GiaBan), 0) 
                   FROM ChiTietDonHang 
                   WHERE MaDH = @maDH)
    WHERE MaDH = @maDH
END
GO

-- ==================================== Phân quyền ====================================
-- Tạo 2 nhóm quyền admin, employee
use QLCHBS
exec sp_addrole 'Admin'

use QLCHBS
exec sp_addrole 'Employee'

-- Cấp quyền cho mỗi nhóm
use QLCHBS
grant control
to Admin

use QLCHBS
grant select
to Employee

-- Tạo 1 admin, 1 employee mặc định

-- Tạo tài khoản login
use QLCHBS
exec sp_addlogin 'admin', 'admin@' 

use QLCHBS
exec sp_addlogin 'employee', 'employee123' 

-- Tạo tài khoản user
use QLCHBS
exec sp_adduser 'admin', 'admin1' 

use QLCHBS
exec sp_adduser 'employee', 'employee1' 

-- add user admin1 vào nhóm quyền Admin
use QLCHBS
exec sp_addrolemember 'Admin', 'admin1'

-- add user admin1 vào nhóm quyền Customer
use QLCHBS
exec sp_addrolemember 'Employee', 'employee1'

-- Để làm bên winform
create table PQ
(
	username char(50),
	pass char(50),
	typeofuser char(10),
	MaNV CHAR(5),
	constraint PK_PQ primary key (MaNV),
	constraint FK_PQ_NhanVien foreign key (MaNV) references NhanVien(MaNV)
)
insert into PQ(username, pass, typeofuser, MaNV) values
('admin1', 'admin@', 'Admin', 'NV001'),
('employee1', 'employee123', 'Employee', 'NV002'),
('employee2', 'employee456', 'Employee', 'NV003')

--=============================ĐẾN ĐÂY===========================================
--=============================CHẠY RIÊNG========================================
GO
--- thêm chi tiết đơn hàng 
CREATE OR ALTER PROCEDURE sp_ThemChiTietDonHang
    @MaDH NVARCHAR(50),
    @MaSach NVARCHAR(50),
    @SoLuong INT
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if SoLuong is greater than SLKho
    IF EXISTS (SELECT 1 FROM Sach WHERE MaSach = @MaSach AND @SoLuong > SLKho)
    BEGIN
        RAISERROR ('Số lượng không được lớn hơn số lượng trong kho.', 16, 1);
        RETURN;
    END

    -- Insert into ChiTietDonHang
    INSERT INTO ChiTietDonHang (MaDH, MaSach, SoLuong, GiaBan)
    SELECT @MaDH, @MaSach, @SoLuong, GiaBan
    FROM Sach
    WHERE MaSach = @MaSach;

    -- Update SLKho in Sach table
    UPDATE Sach
    SET SLKho = SLKho - @SoLuong
    WHERE MaSach = @MaSach;
END;