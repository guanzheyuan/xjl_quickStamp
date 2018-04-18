package utils;

import play.Play;

public class PageSupport {
	public final static  Integer PAGE_COUNT = 10 ;
    public static Integer getPageSize() {
        return PAGE_COUNT;
    }

    public static Integer getPage(Integer page, Long count) {
        return getPage(page, count, getPageSize());
    }

    public static Integer getPage(Integer page, Long count, int pageSize) {
        if (page == null || page <= 0)
            page = new Integer(1);
        if (count == pageSize * (page - 1))
            page = page - 1 > 0 ? page - 1 : 1;
        return page;
    }

    public static Integer getEnd(Integer page, Long count, int pageSize) {
        if(count == 0) return 0;
        int from = (page - 1) * pageSize;
        int end = from + pageSize;
        return (int)( end > count  ? count  : end);
    }
    
    public static Integer getTotalPage(Long totalCount,int countByPage)
	{
		if(totalCount==0)
		return 1 ;
		int totalPage=0 ;
		int pages = totalCount.intValue()/countByPage ;
		totalPage=(totalCount.intValue() % countByPage==0)?pages:pages+1;
		
		return totalPage ;
	}

}