package com.kaisen.common.page;

import java.io.Serializable;

public class Page<T> implements Paginable<T>, Serializable {
	private static final long serialVersionUID = -1304562023131353064L;

	public static final int DEFAULT_PAGE_SIZE = 20; // 默认每页记录数

	public static final int PAGE_COUNT = 10;

	private int pageNo = 1; // 页码

	private int pageSize = DEFAULT_PAGE_SIZE; // 每页记录数

	private int totalCount = 0; // 总记录数

	private int totalPage = 0; // 总页数

	private int startRow = 0;

	private int endRow = 0;

	private String orderStr;

	private boolean isNeedTotalCount = true;

	public boolean isNeedTotalCount() {
		return isNeedTotalCount;
	}

	public void setNeedTotalCount(boolean isNeedTotalCount) {
		this.isNeedTotalCount = isNeedTotalCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		this.setTotalPage(totalPage);
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	@Override
	public boolean isFirstPage() {
		return pageNo <= 1;
	}

	@Override
	public boolean isLastPage() {
		return pageNo >= totalPage;
	}

	@Override
	public int getNextPage() {
		return isLastPage() ? pageNo : (pageNo + 1);
	}

	@Override
	public int getPrePage() {
		return isFirstPage() ? pageNo : (pageNo - 1);
	}

	@Override
	public int getBeginIndex() {
		if (pageNo > 0) {
			return (pageSize * (pageNo - 1));
		} else {
			return 0;
		}
	}

	@Override
	public int getEndIndex() {
		if (pageNo > 0) {
			return Math.min(pageSize * pageNo, totalCount);
		} else {
			return 0;
		}
	}

	public int getBeginPage() {
		if (pageNo > 0) {
			return (PAGE_COUNT * ((pageNo - 1) / PAGE_COUNT)) + 1;
		} else {
			return 0;
		}
	}

	public int getEndPage() {
		if (pageNo > 0) {
			return Math.min(PAGE_COUNT * ((pageNo - 1) / PAGE_COUNT + 1),
					getTotalPage());
		} else {
			return 0;
		}
	}
}
