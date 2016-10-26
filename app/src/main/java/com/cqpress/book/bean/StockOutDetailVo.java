package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockOutDetailVo {

    @SerializedName("Amount")
    private int amount;
    @SerializedName("Book")
    private Book book;
    @SerializedName("BookID")
    private String bookID;
    @SerializedName("DetailID")
    private String detailID;
    @SerializedName("StockOutID")
    private String stockOutID;
    @SerializedName("ScanAmount")
    private int scanAmount;
    @SerializedName("AuditAmount")
    private int auditAmount;

    public int getAuditAmount() {
        return auditAmount;
    }

    public void setAuditAmount(int auditAmount) {
        this.auditAmount = auditAmount;
    }

    public int getScanAmount() {
        return scanAmount;
    }

    public void setScanAmount(int scanAmount) {
        this.scanAmount = scanAmount;
    }

    public static class Book {
        @SerializedName("Author")
        private String author;
        @SerializedName("AuthorSummary")
        private String authorSummary;
        @SerializedName("BookID")
        private String bookID;
        @SerializedName("BookName")
        private String bookName;
        @SerializedName("BookSize")
        private String bookSize;
        @SerializedName("CustomerName")
        private String customerName;
        @SerializedName("EbookUrl")
        private String ebookUrl;
        @SerializedName("ISBN")
        private String isbn;
        @SerializedName("IsPlatformData")
        private boolean isPlatformData;
        @SerializedName("IsUploadData")
        private boolean isUploadData;
        @SerializedName("PublisherID")
        private String publisherID;
        @SerializedName("Publishers")
        private String publishers;
        @SerializedName("RegisterCode")
        private String registerCode;
        @SerializedName("SNTotal")
        private int snTotal;
        @SerializedName("StockAmount")
        private int stockAmount;
        @SerializedName("TakeState")
        private int takeState;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorSummary() {
            return authorSummary;
        }

        public void setAuthorSummary(String authorSummary) {
            this.authorSummary = authorSummary;
        }

        public String getBookID() {
            return bookID;
        }

        public void setBookID(String bookID) {
            this.bookID = bookID;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getBookSize() {
            return bookSize;
        }

        public void setBookSize(String bookSize) {
            this.bookSize = bookSize;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getEbookUrl() {
            return ebookUrl;
        }

        public void setEbookUrl(String ebookUrl) {
            this.ebookUrl = ebookUrl;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public boolean isPlatformData() {
            return isPlatformData;
        }

        public void setPlatformData(boolean platformData) {
            isPlatformData = platformData;
        }

        public boolean isUploadData() {
            return isUploadData;
        }

        public void setUploadData(boolean uploadData) {
            isUploadData = uploadData;
        }

        public String getPublisherID() {
            return publisherID;
        }

        public void setPublisherID(String publisherID) {
            this.publisherID = publisherID;
        }

        public String getPublishers() {
            return publishers;
        }

        public void setPublishers(String publishers) {
            this.publishers = publishers;
        }

        public String getRegisterCode() {
            return registerCode;
        }

        public void setRegisterCode(String registerCode) {
            this.registerCode = registerCode;
        }

        public int getSnTotal() {
            return snTotal;
        }

        public void setSnTotal(int snTotal) {
            this.snTotal = snTotal;
        }

        public int getStockAmount() {
            return stockAmount;
        }

        public void setStockAmount(int stockAmount) {
            this.stockAmount = stockAmount;
        }

        public int getTakeState() {
            return takeState;
        }

        public void setTakeState(int takeState) {
            this.takeState = takeState;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public String getStockOutID() {
        return stockOutID;
    }

    public void setStockOutID(String stockOutID) {
        this.stockOutID = stockOutID;
    }
}
