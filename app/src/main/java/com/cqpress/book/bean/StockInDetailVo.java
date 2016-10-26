package com.cqpress.book.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/8.
 */
public class StockInDetailVo {

    @SerializedName("Amount")
    private int amount;
    @SerializedName("book")
    private Book book;
    @SerializedName("BookID")
    private String bookID;
    @SerializedName("DetailID")
    private String detailID;
    @SerializedName("StockInID")
    private String stockInID;
    @SerializedName("ScanAmount")
    private String scanAmount;

    public String getScanAmount() {
        return scanAmount;
    }

    public void setScanAmount(String scanAmount) {
        this.scanAmount = scanAmount;
    }

    public static class Book {
        @SerializedName("Author")
        private String author;
        @SerializedName("AuthorImageUrl")
        private String authorImageUrl;
        @SerializedName("AuthorSummary")
        private String authorSummary;
        @SerializedName("BookID")
        private String bookID;
        @SerializedName("BookImageUrl")
        private String bookImageUrl;
        @SerializedName("BookInfo")
        private String bookInfo;
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
        @SerializedName("WordCount")
        private String wordCount;

        public String getAuthorImageUrl() {
            return authorImageUrl;
        }

        public void setAuthorImageUrl(String authorImageUrl) {
            this.authorImageUrl = authorImageUrl;
        }

        public String getBookImageUrl() {
            return bookImageUrl;
        }

        public void setBookImageUrl(String bookImageUrl) {
            this.bookImageUrl = bookImageUrl;
        }

        public String getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(String bookInfo) {
            this.bookInfo = bookInfo;
        }

        public String getWordCount() {
            return wordCount;
        }

        public void setWordCount(String wordCount) {
            this.wordCount = wordCount;
        }

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

    public String getStockInID() {
        return stockInID;
    }

    public void setStockInID(String stockInID) {
        this.stockInID = stockInID;
    }
}
