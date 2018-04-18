package utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import play.utils.Java;

public class DateUtil {

	// 时间工具类

	public final static String DATE_FORMAT_1 = "yyyy-MM-dd";

	public final static String DATE_FORMAT_2 = "yyyyMMdd";

	public final static String DATE_FORMAT_4 = "yyyy-MM";

	public final static String DATE_FORMAT_5 = "yyyyMM";

	public final static String DATE_FORMAT_6 = "yyyy";

	public final static String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

	public final static String DATETIME_FORMAT_3 = "yyyy-MM-dd HH-mm-ss";

	public final static String DATETIME_FORMAT_31 = "yyyy-M-d HH-mm-ss";

	public final static String DATETIME_FORMAT_32 = "yyyy-M-d H:m:s";

	public final static String DATETIME_FORMAT_2 = "yyyyMMddHHmmss";

	public final static String DATETIME_FORMAT_4 = "yyyy/MM/dd HH:mm:ss";

	public final static String DATETIME_FORMAT_5 = "yyyy/MM/dd HH:mm";

	public final static String DATE_FORMAT_3 = "yyyy年MM月dd日";

	public static String DEFAULT_DATE_FORMAT = DATE_FORMAT_1;

	public static String DEFAULT_TIME_FORMAT = DATETIME_FORMAT_1;
	//节假日列表  
    private static List<Calendar> holidayList = new ArrayList<Calendar>();   

	public static java.sql.Date getNowDate() {
		return new java.sql.Date(new java.util.Date().getTime());
	}

	public static java.sql.Timestamp getTimestamp() {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}
	/***
	 * 在指定日期格式上加上年、月、星期、日、小时、分钟、秒返回还是日期类型
	 * @param date 要处理的时间日期
	 * @param ymdhmins y:年、m:月、w:星期、d:天、h:小时、min:分钟、s:秒
	 * @param num 要加的数字，可为负数，负数是在指定日期上向前推，正数是在指定日期上向后推
	 * @return 指定时间日期处理后的时间日期（日期类型）
	 */
	public static Date getDateTimeNowFun(Date date,String ymdhmins,int num){
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		c.setTime(date);
		if("y".equals(ymdhmins)){
			c.set(Calendar.YEAR, c.get(Calendar.YEAR)+num);
		}
		else if("m".equals(ymdhmins)){
			c.set(Calendar.MONTH, c.get(Calendar.MONTH)+num);
		}
		else if("w".equals(ymdhmins)){
			c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR)+num);
		}
		else if("d".equals(ymdhmins)){
			c.set(Calendar.DATE, c.get(Calendar.DATE)+num);
		}
		else if("h".equals(ymdhmins)){
			c.set(Calendar.HOUR, c.get(Calendar.HOUR)+num);
		}
		else if("min".equals(ymdhmins)){
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+num);
		}
		else if("s".equals(ymdhmins)){
			c.set(Calendar.SECOND, c.get(Calendar.SECOND)+num);
		}
		return c.getTime();
	}
	/***
	 * 返回当前时间处理增加指定年、月、日后的日期，且是指定格式的
	 * @param format 指定格式
	 * @param ymd 要在年上加传y ，同理月：m ，日：d
	 * @param hmins 要在小时上加传h ，同理分：min ，秒：s
	 * @param num 要在指定元素上增加的数字
	 * @return 
	 */
	public static String getDateTimeNowFun(String ymdhmins,int num){
		return getDateTimeNowFun(ymdhmins,num,null);
	}
	/***
	 * 返回当前时间处理增加指定年、月、日后的日期，且是返回指定格式的
	 * @param ymdhmins 要在年上加传y ，同理月：m ，日：d，要在小时上加传h ，同理分：min ，秒：s
	 * @param num 要在指定元素上增加的数字
	 * @param returnFormat 指定返回的时间格式
	 * @return
	 */
	public static String getDateTimeNowFun(String ymdhmins,int num,String returnFormat){
		String format = "yyyy-MM-dd HH:mm:ss";
		if(returnFormat!=null){
			format = returnFormat;
		}
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		
		if("y".equals(ymdhmins)){
			c.set(Calendar.YEAR, c.get(Calendar.YEAR)+num);
		}
		else if("m".equals(ymdhmins)){
			c.set(Calendar.MONTH, c.get(Calendar.MONTH)+num);
		}
		else if("w".equals(ymdhmins)){
			c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR)+num);
		}
		else if("d".equals(ymdhmins)){
			c.set(Calendar.DATE, c.get(Calendar.DATE)+num);
		}
		else if("h".equals(ymdhmins)){
			c.set(Calendar.HOUR, c.get(Calendar.HOUR)+num);
		}
		else if("min".equals(ymdhmins)){
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+num);
		}
		else if("s".equals(ymdhmins)){
			c.set(Calendar.SECOND, c.get(Calendar.SECOND)+num);
		}
		String out = new SimpleDateFormat(format).format(c.getTime());
		return out;
	}
	

	public static String date2String(Date date) {
		return date2String(date, DEFAULT_DATE_FORMAT);
	}

	public static String date2String(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = getDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获得处理过后的时间字符串(用于最近来访) 如： 1、刚刚 2、n分钟前 3、n小时前 4、今天 08:08 5、昨天 08:08 6、前天
	 * 08:08 7、01-01 08:08
	 * 
	 * @return
	 */
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	@SuppressWarnings("deprecation")
	public static String getDateStr(Date createDate) {
		long nowTime = new Date().getTime();

		long createTime = createDate.getTime();

		long timeDiff = nowTime - createTime;
		if ((timeDiff / 1000) < 60)
			return "刚刚";
		else if ((timeDiff / 1000 / 60) < 60)
			return (timeDiff / 1000 / 60) + "分钟前";
		else if ((timeDiff / 1000 / 60 / 60) < 24)
			return (timeDiff / 1000 / 60 / 60) + "小时前";
		else if ((timeDiff / 1000 / 60 / 60 / 24) < 30)
			return (timeDiff / 1000 / 60 / 60 / 24) + "天前";
		else
			return getCreateDay(createDate) + " " + getCreateTime2(createDate);
	}

	private static String getCreateTime2(Date createDate) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm");
		return sdFormat.format(createDate);
	}

	private static String getCreateDay(Date createDate) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM-dd");
		return sdFormat.format(createDate);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static String getSemesters(String sdate) {
		Date dd = strToDate(sdate);
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(dd);
		int aWeek = c.get(Calendar.WEEK_OF_YEAR);

		String bDate = c.get(Calendar.YEAR) + "-09-01";
		Date bDd = strToDate(bDate);
		Calendar bC = Calendar.getInstance(Locale.CHINA);
		bC.setTime(bDd);
		int bWeek = bC.get(Calendar.WEEK_OF_YEAR);

		String cDate = c.get(Calendar.YEAR) + "-09-01";
		Date cDd = strToDate(cDate);
		Calendar cC = Calendar.getInstance(Locale.CHINA);
		cC.setTime(cDd);
		int cWeek = cC.get(Calendar.WEEK_OF_YEAR);
		return c.get(Calendar.MONTH) + 1 > 8 || c.get(Calendar.MONTH) + 1 < 3
				|| aWeek == bWeek || aWeek == cWeek ? "上学期" : "下学期";
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static String getYears(String sdate) {
		Date dd = strToDate(sdate);
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(dd);
		int aWeek = c.get(Calendar.WEEK_OF_YEAR);

		String bDate = c.get(Calendar.YEAR) + "-09-01";
		Date bDd = strToDate(bDate);
		Calendar bC = Calendar.getInstance(Locale.CHINA);
		bC.setTime(bDd);

		int bWeek = bC.get(Calendar.WEEK_OF_YEAR);

		if (c.get(Calendar.MONTH) + 1 > 8 || aWeek == bWeek) {
			String years = String.valueOf(c.get(Calendar.YEAR)) + "-";
			c.add(Calendar.YEAR, 1);
			years += String.valueOf(c.get(Calendar.YEAR));
			return years;
		} else {
			String years = "-" + String.valueOf(c.get(Calendar.YEAR));
			c.add(Calendar.YEAR, -1);
			years = String.valueOf(c.get(Calendar.YEAR)) + years;
			return years;
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static String getSeqWeek(String sdate) {
		Date dd = strToDate(sdate);
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(dd);
		int week = c.get(Calendar.WEEK_OF_YEAR);

		int weekJilu = 0;

		String beginsDate = c.get(Calendar.YEAR) + "-09-01";
		Date beginsDd = strToDate(beginsDate);
		Calendar beginsC = Calendar.getInstance(Locale.CHINA);
		beginsC.setTime(beginsDd);
		int beginsWeek = beginsC.get(Calendar.WEEK_OF_YEAR);

		String yiYueErYueDate = c.get(Calendar.YEAR) + "-03-01";
		Date yiYueErYue = strToDate(yiYueErYueDate);
		Calendar yiYueErYueC = Calendar.getInstance(Locale.CHINA);
		yiYueErYueC.setTime(yiYueErYue);
		int yiYueErYueWeek = yiYueErYueC.get(Calendar.WEEK_OF_YEAR);

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		if (week <= yiYueErYueWeek && dd.getTime() < yiYueErYue.getTime()) {// 1月到2月底的算法

			Date beginDate = strToDate(c.get(Calendar.YEAR) + "-03-01");
			Calendar date = Calendar.getInstance(Locale.CHINA);
			date.setTime(beginDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);

			String yiYueErYueDiDate = dft.format(date.getTime());// 得到2月底是几号
			Date yiYueErYueDi = strToDate(yiYueErYueDiDate);
			Calendar yiYueErYueDiC = Calendar.getInstance(Locale.CHINA);
			yiYueErYueDiC.setTime(yiYueErYueDi);
			int yiYueErYueDiWeek = yiYueErYueDiC.get(Calendar.WEEK_OF_YEAR);// 得到当年2月底时第几周

			c.add(Calendar.YEAR, -1);
			String years = String.valueOf(c.get(Calendar.YEAR));

		}

		if (week <= beginsWeek && week >= yiYueErYueWeek
				&& dd.getTime() >= yiYueErYue.getTime()
				&& dd.getTime() < beginsDd.getTime()) {

			weekJilu = (week - yiYueErYueWeek) + 1; // 3月1号-8月31号的算法
		}
		if (week >= beginsWeek && dd.getTime() >= beginsDd.getTime()) {// 9月1号的算法
			weekJilu = (week - beginsWeek) + 1;
		}
		if (week >= beginsWeek) {// 9月1号的算法
			weekJilu = (week - beginsWeek) + 1;
		}
		String weeks = Integer.toString(weekJilu);
		if (weeks.length() == 1)
			weeks = "0" + weeks;
		return weeks;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		if (strDate == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDateByFormat(String strDate, String format) {
		if(StringUtil.isEmpty(format))
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		// ParsePosition pos = new ParsePosition(0);
		Date strtodate=new Date();;
		try {
			strtodate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strtodate;
	}

	/**
	 * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	 * 
	 * @param sdate
	 * @param num
	 * @return
	 */
	public static String getWeek(String sdate, String num) {
		// 再转换为时间
		Date dd = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num.equals("1")) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}
	
	/***
	 * 返回时间日期是星期几
	 * @param sdate
	 * @return
	 */
	public static String getWeekOfDate(String sdate) {
		String strWeek = "";
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE",Locale.CHINA);
		strWeek = dateFm.format(strToDate(sdate));
		return strWeek;
	}

	/**
	 * 换成要求格式的时间表示 "yyyy-MM-dd HH:mm:ss"
	 *
	 * @param dateTime
	 *            long：long型格式的时间
	 * @param formatStr
	 *            String
	 * @return String：要求格式的时间字符串
	 */
	public static String getDateTime(Date dateTime, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String strDate = sdf.format(dateTime);
		return strDate;
	}

	/**
	 * 获取时间戳 Jul 6, 2011
	 * 
	 * @return
	 * 
	 *         String
	 */
	public static String getTimeStamp(String format) {
		SimpleDateFormat dateFm = new SimpleDateFormat(format); // 格式化当前系统日期
		return dateFm.format(new java.util.Date());
	}

	/**
	 * 获取时间段天数
	 * 
	 * @param now
	 * @param before
	 * @return
	 */
	public static long diffDays(Date now, Date before) {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long days = 0;
		try {
			// Date d1 = df.parse("2004-03-26 13:31:40");
			// Date d2 = df.parse("2004-01-02 11:30:24");
			long diff = now.getTime() - before.getTime();
			days = diff / (1000 * 60 * 60 * 24);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return days;
	}
	
	public static long diffHours(Date now, Date before) {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long days = 0;
		try {
			// Date d1 = df.parse("2004-03-26 13:31:40");
			// Date d2 = df.parse("2004-01-02 11:30:24");
			long diff = now.getTime() - before.getTime();
			days = diff / (1000 * 60 * 60);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return days;
	}
	/***
	 * 获取两时间间隔分钟数
	 * @param now
	 * @param before
	 * @return
	 */
	public static long diffMins(Date now, Date before) {
		long mins = 0;
		try {
			// Date d1 = df.parse("2004-03-26 13:31:40");
			// Date d2 = df.parse("2004-01-02 11:30:24");
			long diff = now.getTime() - before.getTime();
			mins = diff / (1000 * 60);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mins;
	}
	/***
	 * 获取两时间间隔秒数
	 * @param now
	 * @param before
	 * @return
	 */
	public static long diffSeconds(Date now, Date before) {
		long seconds = 0;
		try {
			// Date d1 = df.parse("2004-03-26 13:31:40");
			// Date d2 = df.parse("2004-01-02 11:30:24");
			long diff = now.getTime() - before.getTime();
			seconds = diff / (1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return seconds;
	}

	/**
	 * 获取时间段内的天数
	 * 
	 * @param now
	 * @param before
	 * @return
	 */
	public static long diffDays(Date createat, Date leaveat, String startdate,
			String enddate, long uid) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long days = -1;
		try {
			Date ds = df.parse(startdate);
			Date de = df.parse(enddate);

			if (df.parse(df.format(createat)).after(de)) {
				days = 0;
			} else if (df.format(createat).equals(enddate)) {
				days = 1;
			} else if (ds.before(createat)
					|| df.format(createat).equals(startdate)) {
				if (leaveat == null) {
					long diff = df.parse(df.format(de)).getTime()
							- df.parse(df.format(createat)).getTime();
					days = diff / (1000 * 60 * 60 * 24) + 1;
				} else if (leaveat.before(de)
						|| df.format(leaveat).equals(enddate)) {
					long diff = df.parse(df.format(leaveat)).getTime()
							- df.parse(df.format(createat)).getTime();
					days = diff / (1000 * 60 * 60 * 24);
				} else {
					long diff = df.parse(df.format(de)).getTime()
							- df.parse(df.format(createat)).getTime();
					days = diff / (1000 * 60 * 60 * 24) + 1;
				}
			} else if (ds.after(createat)
					|| df.format(createat).equals(startdate)) {

				if (leaveat == null) {
					long diff = df.parse(df.format(de)).getTime()
							- df.parse(df.format(ds)).getTime();
					days = diff / (1000 * 60 * 60 * 24) + 1;
				} else if (leaveat.before(ds)
						|| df.format(leaveat).equals(startdate)) {
					return 0;
				} else if ((leaveat.after(ds))
						&& (leaveat.before(de) || df.format(leaveat).equals(
								enddate))) {
					long diff = df.parse(df.format(leaveat)).getTime()
							- df.parse(df.format(ds)).getTime();
					days = diff / (1000 * 60 * 60 * 24);
				} else if (leaveat.after(de)
						|| df.format(leaveat).equals(enddate)) {
					long diff = df.parse(df.format(de)).getTime()
							- df.parse(df.format(ds)).getTime();
					days = diff / (1000 * 60 * 60 * 24) + 1;
				} else {
					long diff = df.parse(df.format(de)).getTime()
							- df.parse(df.format(ds)).getTime();
					days = diff / (1000 * 60 * 60 * 24) + 1;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return days;
	}

	/**
	 * 当前日期-时间
	 * 
	 * @return
	 */
	public static String getCurDateStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int year = cal.get(Calendar.YEAR);// 得到年
		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		int hour = cal.get(Calendar.HOUR);// 得到小时
		int minute = cal.get(Calendar.MINUTE);// 得到分钟
		int second = cal.get(Calendar.SECOND);// 得到秒
		String fullDateStr = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;
		//System.out.println("结果：" + fullDateStr);
		return fullDateStr;
	}

	/**
	 * 当前年
	 * 
	 * @return
	 */
	public static String yearStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int year = cal.get(Calendar.YEAR);// 得到年
		return String.valueOf(year);
	}

	/**
	 * 当前月份
	 * 
	 * @return
	 */
	public static String monthStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类

		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		return String.valueOf(month);
	}

	/**
	 * 当前月份-full
	 * 
	 * @return
	 */
	public static String fullMonthStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类

		int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
		if (month > 9)
			return String.valueOf(month);
		else
			return "0" + String.valueOf(month);
	}

	/**
	 * 当前天，几号
	 * 
	 * @return
	 */
	public static String dayStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
		if (day < 10)
			return "0" + day;

		return String.valueOf(day);
	}

	/**
	 * 当前小时
	 * 
	 * @return
	 */
	public static String hourStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int hour = cal.get(Calendar.HOUR);// 得到小时
		if (hour < 10)
			return "0" + hour;

		return String.valueOf(hour);
	}

	/**
	 * 当前分钟
	 * 
	 * @return
	 */
	public static String minutesStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int minute = cal.get(Calendar.MINUTE);// 得到分钟

		if (minute < 10)
			return "0" + minute;

		return String.valueOf(minute);

	}

	/**
	 * 当前秒
	 * 
	 * @return
	 */
	public static String secondStr() {
		Calendar cal = Calendar.getInstance();// 使用日历类
		int second = cal.get(Calendar.SECOND);// 得到秒
		if (second < 10)
			return "0" + second;

		return String.valueOf(second);
	}

	/**
	 * 根据年-月算最第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String startMonthFullDateStr(int year, int month) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);

		String result = year + "-" + month;
		int begin = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		if (begin < 10) {
			result = result + "-0" + begin;
		} else {
			result = result + "-" + begin;
		}
		play.Logger.info(" begin: " + result);
		return result;
	}

	/**
	 * 根据年-月算最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String endMonthFullDateStr(int year, int month) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);

		int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String result = year + "-" + month + "-" + end;
		play.Logger.info(" end: " + result);
		return result;
	}

	public static java.sql.Timestamp string2Timestamp(String date) {
		java.sql.Date dDate = string2SQLDate(date);
		if (dDate != null) {
			return new java.sql.Timestamp(dDate.getTime());
		}
		return null;
	}

	public static java.sql.Date string2SQLDate(String date) {
		java.sql.Date ret = null;

		if (date == null || date.length() == 0) {
			return null;
		}
		if (date.length() > 11) {
			if (date.indexOf('-') > 0) {

				if (date.indexOf(':') > 0) {
					try {
						ret = string2SQLDate(date, DATETIME_FORMAT_1);
					} catch (Exception e) {
						try {
							ret = string2SQLDate(date, DATETIME_FORMAT_31);
						} catch (Exception e1) {
							ret = string2SQLDate(date, DATETIME_FORMAT_32);
						}
					}
				} else {
					ret = string2SQLDate(date, DATETIME_FORMAT_3);
				}
			} else if (date.indexOf('/') > 0) {
				if (date.trim().length() == 16) {
					ret = string2SQLDate(date, DATETIME_FORMAT_5);
				} else {
					ret = string2SQLDate(date, DATETIME_FORMAT_4);
				}
			} else {
				ret = string2SQLDate(date, DATETIME_FORMAT_2);
			}
		} else {
			if (date.indexOf('-') > 0) {
				if (date.length() == 7) {
					ret = string2SQLDate(date, DATE_FORMAT_4);
				} else {
					ret = string2SQLDate(date, DATE_FORMAT_1);
				}
			} else if (date.length() == 4) {
				ret = string2SQLDate(date, DATE_FORMAT_6);
			} else if (date.length() == 6) {
				ret = string2SQLDate(date, DATE_FORMAT_5);
			} else if (date.length() == 8) {
				ret = string2SQLDate(date, DATE_FORMAT_2);
			} else {
				ret = string2SQLDate(date, DATE_FORMAT_3);
			}
		}
		return ret;
	}

	public static SimpleDateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	public synchronized static java.sql.Date string2SQLDate(String date,
			String format) {
		boolean isSucc = true;
		Exception operateException = null;
		if (!ValidateUtil.validateNotEmpty(format)) {
			isSucc = false;
			operateException = new IllegalArgumentException(
					"the date format string is null!");
		}
		SimpleDateFormat sdf = getDateFormat(format);
		if (!ValidateUtil.validateNotNull(sdf)) {
			isSucc = false;
			operateException = new IllegalArgumentException(
					"the date format string is not matching available format object");
		}
		java.util.Date tmpDate = null;
		try {
			if (isSucc) {
				tmpDate = sdf.parse(date);
				String tmpDateStr = sdf.format(tmpDate);
				if (!tmpDateStr.equals(date)) {
					isSucc = false;
					operateException = new IllegalArgumentException(
							"the date string " + date
									+ " is not matching format: " + format);
				}
			}
		} catch (Exception e) {
			isSucc = false;
			operateException = e;
		}

		if (!isSucc) {
			if (operateException instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) operateException;
			} else {
				throw new IllegalArgumentException("the date string " + date
						+ " is not matching format: " + format
						+ ".\n cause by :" + operateException.toString());
			}
		} else {
			return new java.sql.Date(tmpDate.getTime());
		}

	}
	
	public static java.util.Date parseDate(Object o){
		if (o instanceof java.util.Date||o instanceof Timestamp||o instanceof java.sql.Date)
		{
			java.util.Date date = (java.util.Date)o; 
			return date;
		}
		return null;
	}
	/***
	 * 解决时间类型成指定的格式，如果没有指定输入格式，输出的格式为yyyy-MM-dd HH:mm:ss
	 * @param o
	 * @param format
	 * @return
	 */
	public static String parseDateStr(Object o,String format){
		format = StringUtil.isNotEmpty(format)?format:DATETIME_FORMAT_1;
		if (o instanceof java.util.Date||o instanceof Timestamp||o instanceof java.sql.Date)
		{
			java.util.Date date = (java.util.Date)o; 
			return date2String(date, format);
		}
		return null;
	}
	/***
	 * 判断两个日期是否是同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
	       Calendar cal1 = Calendar.getInstance();
	       cal1.setTime(date1);

	       Calendar cal2 = Calendar.getInstance();
	       cal2.setTime(date2);

	       boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
	               .get(Calendar.YEAR);
	       boolean isSameMonth = isSameYear
	               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	       boolean isSameDate = isSameMonth
	               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
	                       .get(Calendar.DAY_OF_MONTH);

	       return isSameDate;
	   }
	
	 public static List<Date> getDateWeek(Date begin,Date end){
		 if(null == begin){
		     begin = new Date();
		 }
		 if(null == end){
		     end = new Date();
		 }
		 List<Date> list = getDate(begin, end);
		 List<Date> result = new ArrayList<Date>();
		 Calendar calendar = Calendar.getInstance();
		 for(int i=0;i<list.size();i++){
		     calendar.setTime(list.get(i));
		     if (calendar.get(Calendar.DAY_OF_WEEK) == 1
		      || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
		  continue;
		     }else{
		  result.add(list.get(i));
		     }
		 }
		 return result;
		    }
	 /**
	     * 返回两个日期之间的所有天数的日期
	     * @param begin
	     * @param end
	     * @return
	     */
	    @SuppressWarnings("deprecation")
	    public static List<Date> getDate(Date begin , Date end){
	 List<Date> result = new ArrayList<Date>();
	 while(begin.before(end)){
	     result.add(new Date(begin.getYear(),begin.getMonth(),begin.getDate()));
	     begin.setDate(begin.getDate()+1);
	 }
	 result.add(end);
	 return result;
	    }
	    
	    /** 
	    * 获得该月第一天 
	    * @param year 
	    * @param month 
	    * @return 
	    */  
	    public static String getFirstDayOfMonth(int year,int month){  
	            Calendar cal = Calendar.getInstance();  
	            //设置年份  
	            cal.set(Calendar.YEAR,year);  
	            //设置月份  
	            cal.set(Calendar.MONTH, month-1);  
	            //获取某月最小天数  
	            int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);  
	            //设置日历中月份的最小天数  
	            cal.set(Calendar.DAY_OF_MONTH, firstDay);  
	            //格式化日期  
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	            String firstDayOfMonth = sdf.format(cal.getTime());  
	            return firstDayOfMonth;  
	        }  
	      
	    /** 
	    * 获得该月最后一天 
	    * @param year 
	    * @param month 
	    * @return 
	    */  
	    public static String getLastDayOfMonth(int year,int month){  
	            Calendar cal = Calendar.getInstance();  
	            //设置年份  
	            cal.set(Calendar.YEAR,year);  
	            //设置月份  
	            cal.set(Calendar.MONTH, month-1);  
	            //获取某月最大天数  
	            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
	            //设置日历中月份的最大天数  
	            cal.set(Calendar.DAY_OF_MONTH, lastDay);  
	            //格式化日期  
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	            String lastDayOfMonth = sdf.format(cal.getTime());  
	            return lastDayOfMonth;  
	        }  
	    public static List<Date> getDates(int year,int month){    
	        List<Date> dates = new ArrayList<Date>();    
	            
	        Calendar cal = Calendar.getInstance();    
	        cal.set(Calendar.YEAR, year);    
	        cal.set(Calendar.MONTH,  month - 1);    
	        cal.set(Calendar.DATE, 1);    
	            
	            
	        while(cal.get(Calendar.YEAR) == year &&     
	                cal.get(Calendar.MONTH) < month){    
	            int day = cal.get(Calendar.DAY_OF_WEEK);    
	                
	            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){    
	                dates.add((Date)cal.getTime().clone());    
	            }    
	            cal.add(Calendar.DATE, 1);    
	        }    
	        return dates;    
	    
	    } 
	    
    public static boolean checkHoliday(Calendar calendar) throws Exception{
    	initholidayList();
    	  //判断日期是否是周六周日  
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||   
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){  
            return true;  
            //判断是否为节假日
        }
        //判断日期是否是节假日  
        for (Calendar ca : holidayList) {  
           if(ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&  
                   ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)&&  
                   ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){  
               return true;  
           }  
       } 
        return false;
    }
    
    public static void initholidayList(){
		 //春节
	    	initHolidayList("2018-02-15");
	    	initHolidayList("2018-02-16");
	    	initHolidayList("2018-02-17");
	    	initHolidayList("2018-02-18");
	    	initHolidayList("2018-02-19");
	    	initHolidayList("2018-02-20");
	    	initHolidayList("2018-02-21");
	    	//清明
	    	initHolidayList("2018-04-05");
	    	initHolidayList("2018-04-06");
	    	initHolidayList("2018-04-07");
	    	initHolidayList("2018-04-21");
	    	//五月
	    	initHolidayList("2018-04-29");
	    	initHolidayList("2018-04-30");
	    	initHolidayList("2018-05-01");
	    	//端午节
	    	initHolidayList("2018-06-16");
	    	initHolidayList("2018-06-17");
	    	initHolidayList("2018-06-18");
	    	//中秋
	    	initHolidayList("2018-09-22");
	    	initHolidayList("2018-09-23");
	    	initHolidayList("2018-09-24");
	    	//国庆
	    	initHolidayList("2018-10-01");
	    	initHolidayList("2018-10-02");
	    	initHolidayList("2018-10-03");
	    	initHolidayList("2018-10-04");
	    	initHolidayList("2018-10-05");
	    	initHolidayList("2018-10-06");
	    	initHolidayList("2018-10-07");
	   }
    /** 
     *  
     * 把所有节假日放入list 
     * @param date  从数据库查 查出来的格式2016-05-09 
     * return void    返回类型  
     * throws  
     */  
   public static void initHolidayList(String date){  
           String [] da = date.split("-");  
           Calendar calendar = Calendar.getInstance();  
           calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));  
           calendar.set(Calendar.MONTH, Integer.valueOf(da[1])-1);//月份比正常小1,0代表一月  
           calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));  
           holidayList.add(calendar);  
   } 
   public static String showDate(Date date,String pattern){
       String  dateStr=format(date,pattern);
       String year = dateStr.substring(0,4);
       Long yearNum =Long.parseLong(year);
       int month = Integer.parseInt(dateStr.substring(5,7));  
       int day = Integer.parseInt(dateStr.substring(8,10));  
       String hour = dateStr.substring(11,13);  
       String minute = dateStr.substring(14,16);

       long addtime =date.getTime();
       long today = System.currentTimeMillis();//当前时间的毫秒数
       Date now = new Date(today);
       String  nowStr=format(now,pattern);
       int  nowDay = Integer.parseInt(nowStr.substring(8,10));
       String result="";
          long l=today-addtime;//当前时间与给定时间差的毫秒数
          long days=l/(24*60*60*1000);//这个时间相差的天数整数，大于1天为前天的时间了，小于24小时则为昨天和今天的时间
          long hours=(l/(60*60*1000)-days*24);//这个时间相差的减去天数的小时数
          long min=((l/(60*1000))-days*24*60-hours*60);//
          long s=(l/1000-days*24*60*60-hours*60*60-min*60);
          if(days > 0){
                  if(days>0 && days<2){
                      result ="前天"+hour+":"+minute+"";                           
                  } else {
                      result = yearNum+"-"+month+"-"+day+" "+hour+":"+minute+"";
                  }
          }else if(hours > 0 ) {
                   if(day!=nowDay){
                       result = "昨天"+hour+":"+minute+"";
                   }else{
                       result=hours+"小时 前";    
                   }
          } else if(min > 0){
                  if(min>0 && min<15){
                      result="刚刚";
                  } else {
                      result=min+"分 前";
                  }
          }else {
                   result=s+"秒 前";
          }
          return result;
}
   /**
    * 日期格式化
    * @param date      需要格式化的日期
    * @param pattern   时间格式，如：yyyy-MM-dd HH:mm:ss
    * @return          返回格式化后的时间字符串
    */
   public static String format(Date date, String pattern){
       SimpleDateFormat sdf = new SimpleDateFormat(pattern);
       return sdf.format(date);
   }

	public static void main(String[] args)  {
		List<Date> dates  =  DateUtil.getDates(2028,1);
		  System.out.println(dates.size());
	}

}