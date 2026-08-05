package com.mkdp.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonStringUtil {

	private final static char WHITE_SPACE = ' ';

    public static final String EMPTY = "";


    /**
     * <p>Checks if the String contains only uppercase characters.</p>
     *
     */
    public static boolean isAllUpperCase(String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isUpperCase(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 입력된 값을 trim하여 null인지 점검
     * @param str
     * @return 입력값이 널인 경우 참
     */
	public static boolean isNull(String str) {

		if (str != null) {
			str = str.trim();
		}

		return (str == null || "".equals(str));
	}

    /**
     * 입력된 값이 alphabet인지 점검
     *
     * @param str
     * @return 입력값이 alphabet인 경우 참
     */
	public static boolean isAlpha(String str) {

		if (str == null) {
			return false;
		}

		int size = str.length();

		if (size == 0)
			return false;

		for (int i = 0; i < size; i++) {
			if (!Character.isLetter(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}


    /**
     * 입력된 값이 alphanumeric인지 점검
     *
     * @param str
     * @return 입력값이 alphanumeric인 경우 참
     */
	public static boolean isAlphaNumeric(String str) {

		if (str == null) {
			return false;
		}

		int size = str.length();

		if (size == 0)
			return false;

		for (int i = 0; i < size; i++) {
			if (!Character.isLetterOrDigit(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

    /**
     * Integer 형을 string으로 변환
     *
     * @param integer value
     * @return string value
     */
    public static String integer2string(int integer) {
        return ("" + integer);
    }

    /**
     * long type을 string type으로 변환
     *
     * @param longdata
     * @return string
     */
    public static String long2string(long longdata) {
        return String.valueOf(longdata);
    }
    /**
     * float type을 string type으로 변환
     *
     * @param floatdata
     * @return string
     */
    public static String float2string(float floatdata) {
        return String.valueOf(floatdata);
    }
    /**
     * double type을 string type으로 변환
     *
     * @param doubledata
     * @return string
     */
    public static String double2string(double doubledata) {
        return String.valueOf(doubledata);
    }

    /**
     * @param str
     * @return string
     */
	public static String null2void(String str) {

		if (isNull(str)) {
			str = "";
		}

		return str;
	}

    /**
     * String형을 integer 형으로 변환
     *
     * @param str
     * @return integer
     */
	public static int string2integer(String str) {

		if (isNull(str)) {
			return 0;
		}

		return Integer.parseInt(str);
	}

    /**
     * String형을 Float 형으로 변환
     *
     * @param str
     * @return 변환된 float형
     */
	public static float string2float(String str) {

		if (isNull(str)) {
			return 0.0F;
		}

		return Float.parseFloat(str);
	}

    /**
     * String형을 Double 형으로 변환
     *
     * @param str
     * @return 변환된 double형
     */
	public static double string2double(String str) {

		if (isNull(str)) {
			return 0.0D;
		}

		return Double.parseDouble(str);
	}

    /**
     * String형을 long 형으로 변환
     *
     * @param  str
     * @return long value
     */
	public static long string2long(String str) {

		if (isNull(str)) {
			return 0L;
		}

		return Long.parseLong(str);
	}

    /**
     * null을 String으로 변환
     *
     * @param str
     * @param defaultValue
     * @return string
     */
	public static String null2string(String str, String defaultValue) {

		if (isNull(str)) {
			return defaultValue;
		}

		return str;
	}
	/**
	 * null인 경우 default value로 변환
	 *
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static Object null2Object(Object obj, Object defaultValue) {
		String tmpStr = String.valueOf(obj);
		if("null".equals(tmpStr)) {
			String objType = defaultValue.getClass().getTypeName();
			if (objType.equals("java.lang.Double")) {
				return Double.parseDouble(String.valueOf(defaultValue));
			} else if (objType.equals("java.lang.Integer")) {
				return Integer.parseInt(String.valueOf(defaultValue));
			} else if (objType.equals("java.lang.Long")) {
				return Long.parseLong(String.valueOf(defaultValue));
			} else if (objType.equals("java.math.BigDecimal")) {
				return new BigDecimal(String.valueOf(defaultValue));
			} else {
				return defaultValue;
			}
		}
		return obj;
	}


    /**
     * String형을 integer형으로 변환
     *
     * @param str
     * @param defaultValue
     * @return Integer value
     */
	public static int string2integer(String str, int defaultValue) {

		if (isNull(str)) {
			return defaultValue;
		}

		return Integer.parseInt(str);
	}

    /**
     * String형을 float형으로 변환
     *
     * @param str String형 value
     * @param defaultValue Float형 value
     * @return 변환된 Float value(null인 경우 default value)
     */
	public static float string2float(String str, float defaultValue) {

		if (isNull(str)) {
			return defaultValue;
		}

		return Float.parseFloat(str);
	}

    /**
     * String형을 double형으로 변환
     *
     * @param str String형 value
     * @param defaultValue double형 value
     * @return 변환된 double value(null인 경우 default value)
     */
	public static double string2double(String str, double defaultValue) {

		if (isNull(str)) {
			return defaultValue;
		}

		return Double.parseDouble(str);
	}

    /**
     * String형을 Long형으로 변환
     *
     * @param str String형 value
     * @param defaultValue Long형 value
     * @return 변환된 double value(null인 경우 default value)
     */
	public static long string2long(String str, long defaultValue) {

		if (isNull(str)) {
			return defaultValue;
		}

		return Long.parseLong(str);
	}

    /**
     * 두 String형 value가 동일한지 비교
     *
     * @param source
     * @param target
     * @return 동일하면 참
     */
	public static boolean equals(String source, String target) {

		return null2void(source).equals(null2void(target));

	}

    /**
     * 입력된 String Value를 특정 길이 만큼 잘라냄
     *
     * @param str String value
     * @param beginIndex 시작위치
     * @param endIndex 끝위치
     * @return 시작위치에서 끝위치까지 잘라낸 String Value
     */
	public static String toSubString(String str, int beginIndex, int endIndex) {

		if (equals(str, "")) {
			return str;
		} else if (str.length() < beginIndex) {
			return "";
		} else if (str.length() < endIndex) {
			return str.substring(beginIndex);
		} else {
			return str.substring(beginIndex, endIndex);
		}

	}

    /**
     * 입력된 String Value를 특정 위치에서 끝까지 잘라냄
     *
     * @param source String value
     * @param beginIndex 시작위치
     * @return 잘라낸 String Value
     */
	public static String toSubString(String source, int beginIndex) {

		if (equals(source, "")) {
			return source;
		} else if (source.length() < beginIndex) {
			return "";
		} else {
			return source.substring(beginIndex);
		}

	}

    /**
     * Source String Value에서 Target String value가 있는지 검색
     *
     * @param source
     * @param target
     * @return 찾은 위치
     */
	public static int search(String source, String target) {
		int result = 0;
		String strCheck = new String(source);
		for (int i = 0; i < source.length();) {
			int loc = strCheck.indexOf(target);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + target.length();
				strCheck = strCheck.substring(i);
			}
		}
		return result;
	}

    /**
     * 입력된 String value의 앞뒤 공백을 제거함
     *
     * @param str 입력 String
     * @return 공백이 제거된 string
     */
	public static String trim(String str) {
		return str.trim();
	}

    /**
     * 입력된 String value의 왼쪽 공백을 제거함
     *
     * @param str
     * @return string
     */
	public static String ltrim(String str) {

		int index = 0;

		while (' ' == str.charAt(index++));

		if (index > 0)
			str = str.substring(index - 1);

		return str;
	}

	/**
	 * 입력된 String value의 뒤 공백을 제거함
	 *
	 * @param str 입력 string
	 * @return 공백제거된 string
	 */
	public static String rtrim(String str) {

		int index = str.length();

		while (' ' == str.charAt(--index));

		if (index < str.length())
			str = str.substring(0, index + 1);

		return str;
	}

	/**
	 * 두 String value을 합침
	 *
	 * @param str1 첫번째 string
	 * @param str2 두번째 string
	 * @return 합친 string
	 */
	public static String concat(String str1, String str2) {

		StringBuffer sb = new StringBuffer(str1);
		sb.append(str2);

		return sb.toString();
	}

	/**
	 * 입력 string을 특정길이만큼 왼쪽을 공백으로 채움
	 *
	 * @param str 입력 String
	 * @param len 길이
	 * @param pad 공백
	 * @return 공백을 채운 string
	 */
	public static String lPad(String str, int len, char pad) {
		return lPad(str, len, pad, false);
	}

	/**
	 * 입력 string을 trim 여부에 따라 trim 후 특정길이만큼 왼쪽을 공백으로 채움
	 *
	 * @param str 입력 string
	 * @param len 길이
	 * @param pad 공백
	 * @param isTrim trim 여부
	 * @return 공백을 채운 string
	 */
	public static String lPad(String str, int len, char pad, boolean isTrim) {

		if (isNull(str)) {
			return null;
		}

		if (isTrim) {
			str = str.trim();
		}

		for (int i = str.length(); i < len; i++) {
			str = pad + str;
		}

		return str;
	}

	/**
	 * 입력 string을 특정길이만큼 오른쪽으로 공백으로 채움
	 *
	 * @param str 입력 string
	 * @param len 길이
	 * @param pad 공백
	 * @return 공백을 채운 string
	 */
	public static String rPad(String str, int len, char pad) {
		return rPad(str, len, pad, false);
	}

	/**
	 * 입력 string을 trim 여부에 따라 trim 후 특정길이만큼 오른쪽을 공백으로 채움
	 * @param str 입력 string
	 * @param len 길이
	 * @param pad 공백
	 * @param isTrim trim여부
	 * @return 공백을 채운 string
	 */
	public static String rPad(String str, int len, char pad, boolean isTrim) {

		if (isNull(str)) {
			return null;
		}

		if (isTrim) {
			str = str.trim();
		}

		for (int i = str.length(); i < len; i++) {
			str = str + pad;
		}

		return str;
	}

	/**
	 * 입력된 string value를 특정길이를 기준으로 왼쪽 정렬
	 *
	 * @param str 입력 string
	 * @param length 길이
	 * @return 정렬된 string
	 */
	public static String alignLeft(String str, int length) {
		return alignLeft(str, length, false);
	}

	/**
	 * 입력된 string value를 특정길이를 기준으로 왼쪽 정렬
	 *
	 * @param str 입력 string
	 * @param length 길이
	 * @param isEllipsis
	 * @return 정렬된 string
	 */
    public static String alignLeft(String str, int length, boolean isEllipsis) {

        if (str.length() <= length) {

            StringBuffer temp = new StringBuffer(str);
            for (int i = 0; i < (length - str.length()); i++) {
                temp.append(WHITE_SPACE);
            }
            return temp.toString();
        } else {
            if (isEllipsis) {

                StringBuffer temp = new StringBuffer(length);
                temp.append(str.substring(0, length - 3));
                temp.append("...");

                return temp.toString();
            } else {
                return str.substring(0, length);
            }
        }
    }

    /**
     * 입력된 string value를 특정길이를 기준으로 오른쪽 정렬
     *
     * @param str
     * @param length
     * @return 정렬된 string
     */
    public static String alignRight(String str, int length) {

        return alignRight(str, length, false);
    }

    /**
     * 입력된 string value를 특정길이를 기준으로 오른쪽 정렬
     *
     * @param str
     * @param length
     * @param isEllipsis
     * @return string
     */
    public static String alignRight(String str, int length, boolean isEllipsis) {

        if (str.length() <= length) {

            StringBuffer temp = new StringBuffer(length);
            for (int i = 0; i < (length - str.length()); i++) {
                temp.append(WHITE_SPACE);
            }
            temp.append(str);
            return temp.toString();
        } else {
            if (isEllipsis) {

                StringBuffer temp = new StringBuffer(length);
                temp.append(str.substring(0, length - 3));
                temp.append("...");
                return temp.toString();
            } else {
                return str.substring(0, length);
            }
        }
    }

    /**
     * 입력된 string value를 특정길이를 기준으로 중앙정렬
     *
     * @param str
     * @param length
     * @return 정렬된 string
     */
    public static String alignCenter(String str, int length) {
        return alignCenter(str, length, false);
    }

    /**
     * 입력된 string value를 특정길이를 기준으로 중앙정렬
     *
     * @param str
     * @param length
     * @param isEllipsis
     * @return 정렬된 string
     */
    public static String alignCenter(String str, int length, boolean isEllipsis) {
        if (str.length() <= length) {

            StringBuffer temp = new StringBuffer(length);
            int leftMargin = (int) (length - str.length()) / 2;

            int rightMargin;
            if ((leftMargin * 2) == (length - str.length())) {
                rightMargin = leftMargin;
            } else {
                rightMargin = leftMargin + 1;
            }

            for (int i = 0; i < leftMargin; i++) {
                temp.append(WHITE_SPACE);
            }

            temp.append(str);

            for (int i = 0; i < rightMargin; i++) {
                temp.append(WHITE_SPACE);
            }

            return temp.toString();
        } else {
            if (isEllipsis) {

                StringBuffer temp = new StringBuffer(length);
                temp.append(str.substring(0, length - 3));
                temp.append("...");
                return temp.toString();
            } else {
                return str.substring(0, length);
            }
        }

    }

    /**
     * 입력된 String을 대문자로 변환
     *
     * @param str 입력 string
     * @return 변환된 string
     *
     */
    public static String capitalize(String str) {
        return !isNull(str) ? str.substring(0, 1).toUpperCase()
            + str.substring(1).toLowerCase() : str;
    }

    /**
     * 입력 스트링에서 패턴과 일치하는 String이 존재하는 지 검사
     *
     * @param str
     * @param pattern
     * @return 존재하면 참 아니면 거짓
     * @throws Exception
     */
	public static boolean isPatternMatch(String str, String pattern) throws Exception {

		Matcher matcher = Pattern.compile(pattern).matcher(str);

		return matcher.matches();
	}

	/**
	 * 한글스트링을 유니코드로 변환
	 *
	 * @param kor 입력 한글 string
	 * @return 유니코드 String
	 * @throws UnsupportedEncodingException
	 */
    public static String toEng(String kor) throws UnsupportedEncodingException {

        if (isNull(kor)) {
            return null;
        }

        return new String(kor.getBytes("KSC5601"), "8859_1");

    }

    /**
     * 입력 유니코드 string value를 한글로 변환
     *
     * @param en
     * @return 변환된 한글 string
     * @throws UnsupportedEncodingException
     */
    public static String toKor(String en) throws UnsupportedEncodingException {

        if (isNull(en)) {
            return null;
        }

        return new String(en.getBytes("8859_1"), "euc-kr");
    }

    /**
     * 입력된 string에서 특정 문자가 들어있는 갯수를 return
     *
     * @param str 입력 스트링
     * @param charToFind
     * @return integer 개수
     */
    public static int countOf(String str, String charToFind) {
        int findLength = charToFind.length();
        int count = 0;

        for (int idx = str.indexOf(charToFind); idx >= 0; idx =
            str.indexOf(charToFind, idx + findLength)) {
            count++;
        }

        return count;
    }


    /**
     * Encode a string using algorithm specified in web.xml and return the resulting encrypted password.
     * If exception, the plain credentials string is returned
     * @param password Password or other credentials to use in authenticating this username
     * @param algorithm Algorithm used to do the digest
     * @return encypted password based on the algorithm.
     */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {

			return password;
		}

		md.reset();

		// call the update method one or more times (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if (((int) encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString((int) encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}

    /**
     * Encode a string using Base64 encoding. Used when storing passwords as cookies.
     * This is weak encoding in that anyone can use the decodeString routine to reverse the encoding.
     * @param str
     *        String to be encoded
     * @return String encoding result
     */
//	@SuppressWarnings("restriction")
//	@Deprecated
//	public static String encodeString(String str) {
//		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//		return new String(encoder.encodeBuffer(str.getBytes())).trim();
//	}

    /**
     * Decode a string using Base64 encoding.
     * @param str String to be decoded
     * @return String decoding String
     */
//	@SuppressWarnings("restriction")
//	@Deprecated
//	public static String decodeString(String str) {
//		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
//		try {
//			return new String(dec.decodeBuffer(str));
//		} catch (IOException io) {
//			throw new RuntimeException(io.getMessage(), io.getCause());
//		}
//	}

    /**
     * convert first letter to a big letter or a small letter.<br>
     *
     * <pre>
     * StringUtil.trim('Password') = 'password'
     * StringUtil.trim('password') = 'Password'
     * </pre>
     * @param str String to be swapped
     * @return String converting result
     */
	public static String swapFirstLetterCase(String str) {
		StringBuffer sbuf = new StringBuffer(str);
		sbuf.deleteCharAt(0);
		if (Character.isLowerCase(str.substring(0, 1).toCharArray()[0])) {
			sbuf.insert(0, str.substring(0, 1).toUpperCase());
		} else {
			sbuf.insert(0, str.substring(0, 1).toLowerCase());
		}
		return sbuf.toString();
	}

    /**
     * If original String has a specific String, remove specific Strings from original String.
     *
     * <pre>
     * StringUtil.trim('pass*word', '*') = 'password'
     * </pre>
     * @param origString original String
     * @param trimString String to be trimmed
     * @return converting result
     */
	public static String trim(String origString, String trimString) {
		int startPosit = origString.indexOf(trimString);
		if (startPosit != -1) {
			int endPosit = trimString.length() + startPosit;
			return origString.substring(0, startPosit) + origString.substring(endPosit);
		}
		return origString;
	}

    /**
     * Break a string into specific tokens and return a String of last location.
     *
     * <pre>
     * StringUtil.getLastString('password*password*a*b*c', '*') = 'c'
     * </pre>
     * @param origStr original String
     * @param strToken specific tokens
     * @return String of last location
     */
	public static String getLastString(String origStr, String strToken) {
		StringTokenizer str = new StringTokenizer(origStr, strToken);
		String lastStr = "";
		while (str.hasMoreTokens()) {
			lastStr = str.nextToken();
		}
		return lastStr;
	}

    /**
     * If original String has token, Break a string into specific tokens and change String Array.
     * If not, return a String Array which has original String as it is.
     *
     * <pre>
     * StringUtil.getStringArray('passwordabcpassword', 'abc') = String[]{'password','password'}
     * StringUtil.getStringArray('pasword*password', 'abc') = String[]{'pasword*password'}
     * </pre>
     * @param str original String
     * @param strToken specific String token
     * @return String[]
     */
	public static String[] getStringArray(String str, String strToken) {
		if (str.indexOf(strToken) != -1) {
			StringTokenizer st = new StringTokenizer(str, strToken);
			String[] stringArray = new String[st.countTokens()];
			for (int i = 0; st.hasMoreTokens(); i++) {
				stringArray[i] = st.nextToken();
			}
			return stringArray;
		}
		return new String[] { str };
	}

    /**
     * If string is null or empty string, return false. <br>
     * If not, return true.
     *
     * <pre>
     * StringUtil.isNotEmpty('') = false
     * StringUtil.isNotEmpty(null) = false
     * StringUtil.isNotEmpty('abc') = true
     * </pre>
     * @param str original String
     * @return which empty string or not.
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


	/**
     *  It returns true if str matches the pattern string. It performs regular expression pattern matching.
     *
     * <pre>
     * StringUtil.isPatternMatching('abc-def', '*-*') 	= true
     * StringUtil.isPatternMatching('abc', '*-*') 	= false
     * </pre>
     * @param str original String
     * @param pattern pattern String
     * @return boolean which matches the pattern string or not.
     * @throws Exception fail to check pattern matched
     */
	public static boolean isPatternMatching(String str, String pattern) throws Exception {
		// if url has wild key, i.e. "*", convert it to ".*" so that we can perform regex matching
		if (pattern.indexOf('*') >= 0) {
			pattern = pattern.replaceAll("\\*", ".*");
		}

		pattern = "^" + pattern + "$";

		return Pattern.matches(pattern, str);
	}

    /**
     * It returns true if string contains a sequence of the same character.
     *
     * <pre>
     * StringUtil.containsMaxSequence('password', '2') 	= true
     * StringUtil.containsMaxSequence('my000', '3') 	= true
     * StringUtil.containsMaxSequence('abbbbc', '5')	= false
     * </pre>
     * @param str original String
     * @param maxSeqNumber a sequence of the same character
     * @return which contains a sequence of the same character
     */
	public static boolean containsMaxSequence(String str, String maxSeqNumber) {
		int occurence = 1;
		int max = string2integer(maxSeqNumber);
		if (str == null) {
			return false;
		}

		int sz = str.length();
		for (int i = 0; i < (sz - 1); i++) {
			if (str.charAt(i) == str.charAt(i + 1)) {
				occurence++;

				if (occurence == max)
					return true;
			} else {
				occurence = 1;
			}
		}
		return false;
	}

    /**
     * <p>
     * Checks that the String contains certain  characters.
     * </p>
     * <p>
     * A <code>null</code> String will return <code>false</code>. A <code>null</code> invalid character array will return <code>false</code>.
     * An empty String ("") always returns false.
     * </p>
     *
     * <pre>
     * StringUtil.containsInvalidChars(null, *) = false
     * StringUtil.containsInvalidChars(*, null) = false
     * StringUtil.containsInvalidChars(&quot;&quot;, *) = false
     * StringUtil.containsInvalidChars(&quot;ab&quot;, '') = false
     * StringUtil.containsInvalidChars(&quot;abab&quot;, 'xyz') = false
     * StringUtil.containsInvalidChars(&quot;ab1&quot;, 'xyz') = false
     * StringUtil.containsInvalidChars(&quot;xbz&quot;, 'xyz') = true
     * </pre>
     * @param str the String to check, may be null
     * @param invalidChars an array of invalid chars, may be null
     * @return false if it contains none of the invalid chars, or is null
     */
	public static boolean containsInvalidChars(String str, char[] invalidChars) {
		if (str == null || invalidChars == null) {
			return false;
		}
		int strSize = str.length();
		int validSize = invalidChars.length;
		for (int i = 0; i < strSize; i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < validSize; j++) {
				if (invalidChars[j] == ch) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * <p>
     * Checks that the String contains certain characters.
     * </p>
     * <p>
     * A <code>null</code> String will return <code>false</code>. A <code>null</code> invalid character array will return <code>false</code>.
     * An empty String ("") always returns false.
     * </p>
     *
     * <pre>
     * StringUtil.containsInvalidChars(null, *) = false
     * StringUtil.containsInvalidChars(*, null) = false
     * StringUtil.containsInvalidChars(&quot;&quot;, *) = false
     * StringUtil.containsInvalidChars(&quot;ab&quot;, '')  = false
     * StringUtil.containsInvalidChars(&quot;abab&quot;, 'xyz') = false
     * StringUtil.containsInvalidChars(&quot;ab1&quot;, 'xyz') = false
     * StringUtil.containsInvalidChars(&quot;xbz&quot;, 'xyz') = true
     * </pre>
     * @param str the String to check, may be null
     * @param invalidChars a String of invalid chars, may be null
     * @return false if it contains none of the invalid chars, or is null
     */
	public static boolean containsInvalidChars(String str, String invalidChars) {
		if (str == null || invalidChars == null) {
			return true;
		}
		return containsInvalidChars(str, invalidChars.toCharArray());
	}

     /**
     * <p>
     * Checks if the String contains only unicode digits. A decimal point is not a unicode digit and returns false.
     * </p>
     * <p>
     *  <code>null</code> will return <code>false</code>. An empty String ("") will return <code>false</code>.
     * </p>
     *
     * <pre>
     * StringUtil.isNumeric(null) = false
     * StringUtil.isNumeric(&quot;&quot;) = false
     * StringUtil.isNumeric(&quot;  &quot;) = false
     * StringUtil.isNumeric(&quot;123&quot;)  = true
     * StringUtil.isNumeric(&quot;12 3&quot;) = false
     * StringUtil.isNumeric(&quot;ab2c&quot;) = false
     * StringUtil.isNumeric(&quot;12-3&quot;) = false
     * StringUtil.isNumeric(&quot;12.3&quot;) = false
     * </pre>
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		if (sz == 0)
			return false;
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

    /**
     * <p>
     * Reverses a String as per
     * {@link StringBuffer#reverse()}.
     * </p>
     * <p>
     * <A code>null</code> String returns <code>null</code>.
     * </p>
     *
     * <pre>
     * StringUtil.reverse(null) = null
     * StringUtil.reverse(&quot;&quot;) = &quot;&quot;
     * StringUtil.reverse(&quot;bat&quot;) = &quot;tab&quot;
     * </pre>
     * @param str the String to reverse, may be null
     * @return the reversed String, <code>null</code> if null String input
     */

	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuffer(str).reverse().toString();
	}

    /**
     * Make a new String that filled original to a special char as cipers
     * @param originalStr original String
     * @param ch a special char
     * @param cipers cipers
     * @return filled String
     */
	public static String fillString(String originalStr, char ch, int cipers) {
		int originalStrLength = originalStr.length();

		if (cipers < originalStrLength)
			return null;

		int difference = cipers - originalStrLength;

		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < difference; i++)
			strBuf.append(ch);

		strBuf.append(originalStr);
		return strBuf.toString();
	}

    /**
     * Determine whether a (trimmed) string is empty
     * @param foo The text to check.
     * @return Whether empty.
     */
	public static final boolean isEmptyTrimmed(String foo) {
		return (foo == null || foo.trim().length() == 0);
	}

    /**
     * Return token list
     *
     * @param lst
     * @param separator
     * @return List<String>
     */
	public static List<String> getTokens(String lst, String separator) {
		List<String> tokens = new ArrayList<String>();

		if (lst != null) {
			StringTokenizer st = new StringTokenizer(lst, separator);
			while (st.hasMoreTokens()) {
				try {
					String en = st.nextToken().trim();
					tokens.add(en);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return tokens;
	}

    /**
     * Return token list which is separated by 콤마
     *
     * @param lst 입력변수
     * @return token list
     */
	public static List<?> getTokens(String lst) {
        return getTokens(lst, ",");
    }

	/**
	 * underscore가 포함되어 있는 문자열을 Camel Case로 변환
     *
     * @param underScore underscore가 포함된 변수
     * @return Camel 표기법 변수명
     */
    public static String convert2CamelCase(String underScore) {

        if (underScore.indexOf('_') < 0
            && Character.isLowerCase(underScore.charAt(0))) {
            return underScore;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        int len = underScore.length();

        for (int i = 0; i < len; i++) {
            char currentChar = underScore.charAt(i);
            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }
        return result.toString();
    }

    /**
     * 낙타표기법으로 변환
     *
     * @param targetString
     * @param posChar
     * @return 변환 string
     */
	public static String convertToCamelCase(String targetString, char posChar) {
		StringBuffer result = new StringBuffer();
		boolean nextUpper = false;
		String allLower = targetString.toLowerCase();

		for (int i = 0; i < allLower.length(); i++) {
			char currentChar = allLower.charAt(i);
			if (currentChar == posChar) {
				nextUpper = true;
			} else {
				if (nextUpper) {
					currentChar = Character.toUpperCase(currentChar);
					nextUpper = false;
				}
				result.append(currentChar);
			}
		}
		return result.toString();
	}

    /**
     * Convert a string that may contain underscores to camel case.
     * @param underScore Underscore name.
     * @return Camel case representation of the underscore string.
     */
    public static String convertToCamelCase(String underScore) {
        return convertToCamelCase(underScore, '_');
    }

    /**
     * Convert a camel case string to underscore representation.
     * @param camelCase Camel case name.
     * @return Underscore representation of the camel case string.
     */
	public static String convertToUnderScore(String camelCase) {
		String result = "";
		for (int i = 0; i < camelCase.length(); i++) {
			char currentChar = camelCase.charAt(i);
			// This is starting at 1 so the result does not end up with an underscore at the begin of the value
			if (i > 0 && Character.isUpperCase(currentChar)) {
				result = result.concat("_");
			}
			result = result.concat(Character.toString(currentChar).toLowerCase());
		}
		return result;
	}

	public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }

	/**
     * 문자열이 지정한 길이를 초과했을때 지정한길이에다가 해당 문자열을 붙여주는 메서드.
     * @param source 원본 문자열 배열
     * @param output 더할문자열
     * @param slength 지정길이
     * @return 지정길이로 잘라서 더할분자열 합친 문자열
     */
    public static String cutString(String source, String output, int slength) {
        String returnVal = null;
        if (source != null) {
            if (source.length() > slength) {
                returnVal = source.substring(0, slength) + output;
            } else
                returnVal = source;
        }
        return returnVal;
    }

    /**
     * 문자열이 지정한 길이를 초과했을때 해당 문자열을 삭제하는 메서드
     * @param source 원본 문자열 배열
     * @param slength 지정길이
     * @return 지정길이로 잘라서 더할분자열 합친 문자열
     */
    public static String cutString(String source, int slength) {
        String result = null;
        if (source != null) {
            if (source.length() > slength) {
                result = source.substring(0, slength);
            } else
                result = source;
        }
        return result;
    }

    /**
     * <p>
     * String이 비었거나("") 혹은 null 인지 검증한다.
     * </p>
     *
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str - 체크 대상 스트링오브젝트이며 null을 허용함
     * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * <p>기준 문자열에 포함된 모든 대상 문자(char)를 제거한다.</p>
     *
     * <pre>
     * StringUtil.remove(null, *)       = null
     * StringUtil.remove("", *)         = ""
     * StringUtil.remove("queued", 'u') = "qeed"
     * StringUtil.remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param str  입력받는 기준 문자열
     * @param remove  입력받는 문자열에서 제거할 대상 문자열
     * @return 제거대상 문자열이 제거된 입력문자열. 입력문자열이 null인 경우 출력문자열은 null
     */
    public static String remove(String str, char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * 문자열 내부의 콤마 character(,)를 모두 제거한다.
     *
     * @param str 입력받는 기준 문자열
     * @return " , "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeCommaChar(String str) {
        return remove(str, ',');
    }
    /**
     * 문자열 내부의 Dot character(.)를 모두 제거한다.
     *
     * @param str 입력받는 기준 문자열
     * @return " , "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeDotChar(String str) {
        return remove(str, '.');
    }
    /**
     * 문자열 내부의 마이너스 character(-)를 모두 제거한다.
     *
     * @param str  입력받는 기준 문자열
     * @return " - "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeDashChar(String str) {
        return remove(str, '-');
    }
    /**
     * 문자열 내부의 공백 character( )를 모두 제거한다.
     *
     * @param str  입력받는 기준 문자열
     * @return " - "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeSpaceChar(String str) {
        return remove(str, ' ');
    }
    /**
     * 문자열 내부의 Slash character(/)를 모두 제거한다.
     *
     * @param str  입력받는 기준 문자열
     * @return " / "가 제거된 입력문자열
     *  입력문자열이 null인 경우 출력문자열은 null
     */
    public static String removeSlashChar(String str) {
        return remove(str, '/');
    }

    /**
     * 원본 문자열의 포함된 특정 문자열을 새로운 문자열로 변환하는 메서드
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열
     */
    public static String replace(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;

        while (srcStr.indexOf(subject) >= 0) {
            preStr = srcStr.substring(0, srcStr.indexOf(subject));
            nextStr = srcStr.substring(srcStr.indexOf(subject) + subject.length(), srcStr.length());
            srcStr = nextStr;
            rtnStr.append(preStr).append(object);
        }
        rtnStr.append(nextStr);
        return rtnStr.toString();
    }

    /**
     * 원본 문자열의 포함된 특정 문자열 첫번째 한개만 새로운 문자열로 변환하는 메서드
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열 / source 특정문자열이 없는 경우 원본 문자열
     */
    public static String replaceOnce(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        if (source.indexOf(subject) >= 0) {
            preStr = source.substring(0, source.indexOf(subject));
            nextStr = source.substring(source.indexOf(subject) + subject.length(), source.length());
            rtnStr.append(preStr).append(object).append(nextStr);
            return rtnStr.toString();
        } else {
            return source;
        }
    }

    /**
     * <code>subject</code>에 포함된 각각의 문자를 object로 변환한다.
     *
     * @param source 원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object 변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열
     */
    public static String replaceChar(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;

        char chA;

        for (int i = 0; i < subject.length(); i++) {
            chA = subject.charAt(i);

            if (srcStr.indexOf(chA) >= 0) {
                preStr = srcStr.substring(0, srcStr.indexOf(chA));
                nextStr = srcStr.substring(srcStr.indexOf(chA) + 1, srcStr.length());
                srcStr = rtnStr.append(preStr).append(object).append(nextStr).toString();
            }
        }

        return srcStr;
    }

    /**
     * <code>str</code> 중 <code>searchStr</code>의 시작(index) 위치를 반환.
     *
     * 입력값 중 <code>null</code>이 있을 경우 <code>-1</code>을 반환
     *
     * @param str  검색 문자열
     * @param searchStr  검색 대상문자열
     * @return 검색 문자열 중 검색 대상문자열이 있는 시작 위치 검색대상 문자열이 없거나 null인 경우 -1
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }


    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("하이", "하이", null, "bar") = null
     * StringUtil.decode("하이", "하이  ", "foo", null) = null
     * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
     * StringUtil.decode("하이", "하이  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 defaultStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
        if (sourceStr == null && compareStr == null) {
            return returnStr;
        }

        if (sourceStr == null && compareStr != null) {
            return defaultStr;
        }

        if (sourceStr.trim().equals(compareStr)) {
            return returnStr;
        }

        return defaultStr;
    }

    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>sourceStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("하이", "하이", "foo") = "foo"
     * StringUtil.decode("하이", "하이 ", "foo") = "하이"
     * StringUtil.decode("하이", "바이", "foo") = "하이"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 sourceStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) {
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }

    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     * @param object 원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String string = "";

        if (object != null) {
            string = object.toString().trim();
        }

        return string;
    }

    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static String nullConvert(Object src) {

		if (src != null && src instanceof java.math.BigDecimal) {
		    return ((BigDecimal)src).toString();
		}

		if (src == null || src.equals("null")) {
		    return "";
		} else {
		    return ((String)src).trim();
		}
    }

    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static String nullConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
		    return "";
		} else {
		    return src.trim();
		}
    }

    /**
     *<pre>
     * 인자로 받은 String이 null일 경우 &quot;&quot;로 리턴한다.
     * &#064;param src null값일 가능성이 있는 String 값.
     * &#064;return 만약 String이 null 값일 경우 &quot;&quot;로 바꾼 String 값.
     *</pre>
     */
    public static int zeroConvert(Object src) {

		if (src == null || src.equals("null")) {
		    return 0;
		} else {
		    return Integer.parseInt(((String)src).trim());
		}
    }


    public static int zeroConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
		    return 0;
		} else {
		    return Integer.parseInt(src.trim());
		}
    }

    /**
     * <p>문자열에서 {@link Character#isWhitespace(char)}에 정의된
     * 모든 공백문자를 제거한다.</p>
     *
     * <pre>
     * StringUtil.removeWhitespace(null)         = null
     * StringUtil.removeWhitespace("")           = ""
     * StringUtil.removeWhitespace("abc")        = "abc"
     * StringUtil.removeWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str  공백문자가 제거도어야 할 문자열
     * @return the 공백문자가 제거된 문자열, null이 입력되면 <code>null</code>이 리턴
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }

        return new String(chs, 0, count);
    }

    /**
     * Html 코드가 들어간 문서를 표시할때 태그에 손상없이 보이기 위한 메서드
     *
     * @param strString
     * @return HTML 태그를 치환한 문자열
     */
    public static String checkHtmlView(String strString) {
		String strNew = "";

		try {
		    StringBuffer strTxt = new StringBuffer("");

		    char chrBuff;
		    int len = strString.length();

		    for (int i = 0; i < len; i++) {
			chrBuff = (char)strString.charAt(i);

			switch (chrBuff) {
			case '<':
			    strTxt.append("&lt;");
			    break;
			case '>':
			    strTxt.append("&gt;");
			    break;
			case '"':
			    strTxt.append("&quot;");
			    break;
			case 10:
			    strTxt.append("<br>");
			    break;
			case ' ':
			    strTxt.append("&nbsp;");
			    break;
			//case '&' :
			    //strTxt.append("&amp;");
			    //break;
			default:
			    strTxt.append(chrBuff);
			}
		    }

		    strNew = strTxt.toString();

		} catch (Exception ex) {
		    return null;
		}

		return strNew;
    }


    /**
     * 문자열을 지정한 분리자에 의해 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @return result 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator) throws NullPointerException {
        String[] returnVal = null;
        int cnt = 1;

        int index = source.indexOf(separator);
        int index0 = 0;
        while (index >= 0) {
            cnt++;
            index = source.indexOf(separator, index + 1);
        }
        returnVal = new String[cnt];
        cnt = 0;
        index = source.indexOf(separator);
        while (index >= 0) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);

        return returnVal;
    }

    /**
     * <p>{@link String#toLowerCase()}를 이용하여 소문자로 변환한다.</p>
     *
     * <pre>
     * StringUtil.lowerCase(null)  = null
     * StringUtil.lowerCase("")    = ""
     * StringUtil.lowerCase("aBc") = "abc"
     * </pre>
     *
     * @param str 소문자로 변환되어야 할 문자열
     * @return 소문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    /**
     * <p>{@link String#toUpperCase()}를 이용하여 대문자로 변환한다.</p>
     *
     * <pre>
     * StringUtil.upperCase(null)  = null
     * StringUtil.upperCase("")    = ""
     * StringUtil.upperCase("aBc") = "ABC"
     * </pre>
     *
     * @param str 대문자로 변환되어야 할 문자열
     * @return 대문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toUpperCase();
    }

    /**
     * <p>입력된 String의 앞쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripStart(null, *)          = null
     * StringUtil.stripStart("", *)            = ""
     * StringUtil.stripStart("abc", "")        = "abc"
     * StringUtil.stripStart("abc", null)      = "abc"
     * StringUtil.stripStart("  abc", null)    = "abc"
     * StringUtil.stripStart("abc  ", null)    = "abc  "
     * StringUtil.stripStart(" abc ", null)    = "abc "
     * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }

        return str.substring(start);
    }


    /**
     * <p>입력된 String의 뒤쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripEnd(null, *)          = null
     * StringUtil.stripEnd("", *)            = ""
     * StringUtil.stripEnd("abc", "")        = "abc"
     * StringUtil.stripEnd("abc", null)      = "abc"
     * StringUtil.stripEnd("  abc", null)    = "  abc"
     * StringUtil.stripEnd("abc  ", null)    = "abc"
     * StringUtil.stripEnd(" abc ", null)    = " abc"
     * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }

        return str.substring(0, end);
    }

    /**
     * <p>입력된 String의 앞, 뒤에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.strip(null, *)          = null
     * StringUtil.strip("", *)            = ""
     * StringUtil.strip("abc", null)      = "abc"
     * StringUtil.strip("  abc", null)    = "abc"
     * StringUtil.strip("abc  ", null)    = "abc"
     * StringUtil.strip(" abc ", null)    = "abc"
     * StringUtil.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String strip(String str, String stripChars) {
		if (isEmpty(str)) {
		    return str;
		}

		String srcStr = str;
		srcStr = stripStart(srcStr, stripChars);

		return stripEnd(srcStr, stripChars);
    }

    /**
     * 문자열을 지정한 분리자에 의해 지정된 길이의 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @param arraylength 배열 길이
     * @return 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
        String[] returnVal = new String[arraylength];
        int cnt = 0;
        int index0 = 0;
        int index = source.indexOf(separator);
        while (index >= 0 && cnt < (arraylength - 1)) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);
        if (cnt < (arraylength - 1)) {
            for (int i = cnt + 1; i < arraylength; i++) {
                returnVal[i] = "";
            }
        }

        return returnVal;
    }


    /**
     * 문자열 A에서 Z사이의 랜덤 문자열을 구하는 기능을 제공 시작문자열과 종료문자열 사이의 랜덤 문자열을 구하는 기능
     *
     * @param startChr
     *            - 첫 문자
     * @param endChr
     *            - 마지막문자
     * @return 랜덤문자
     * @exception MyException
     */
    public static String getRandomStr(char startChr, char endChr) {

		int randomInt;
		String randomStr = null;


		int startInt = Integer.valueOf(startChr);
		int endInt = Integer.valueOf(endChr);


		if (startInt > endInt) {
		    throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
		}

		try {

		    SecureRandom rnd = new SecureRandom();

		    do {

			randomInt = rnd.nextInt(endInt + 1);
		    } while (randomInt < startInt);


		    randomStr = (char)randomInt + "";
		} catch (Exception e) {

		    throw new RuntimeException(e);
		}
		return randomStr;
    }

    /**
     * 문자열을 다양한 문자셋(EUC-KR[KSC5601],UTF-8..)을 사용하여 인코딩하는 기능 역으로 디코딩하여 원래의 문자열을
     * 복원하는 기능을 제공함 String temp = new String(문자열.getBytes("바꾸기전 인코딩"),"바꿀 인코딩");
     * String temp = new String(문자열.getBytes("8859_1"),"KSC5601"); => UTF-8 에서
     * EUC-KR
     *
     * @param srcString
     *            - 문자열
     * @param srcCharsetNm
     *            - 원래 CharsetNm
     * @param cnvrCharsetNm
     *            - CharsetNm
     * @return 인(디)코딩 문자열
     * @exception exception
     */
    public static String getEncdDcd(String srcString, String srcCharsetNm, String cnvrCharsetNm) {

		String rtnStr = null;

		if (srcString == null)
		    return null;

		try {
		    rtnStr = new String(srcString.getBytes(srcCharsetNm), cnvrCharsetNm);
		} catch (UnsupportedEncodingException e) {
		    rtnStr = null;
		}

		return rtnStr;
    }

    /**
     * 특수문자를 웹 브라우저에서 정상적으로 보이기 위해 특수문자를 처리('<' -> & lT)하는 기능이다
     * @param 	srcString 		- '<'
     * @return 	변환문자열('<' -> "&lt"
     * @exception MyException
     */
    public static String getSpclStrCnvr(String srcString) {

		String rtnStr = null;

		try {
		    StringBuffer strTxt = new StringBuffer("");

		    char chrBuff;
		    int len = srcString.length();

		    for (int i = 0; i < len; i++) {
			chrBuff = (char)srcString.charAt(i);

			switch (chrBuff) {
			case '<':
			    strTxt.append("&lt;");
			    break;
			case '>':
			    strTxt.append("&gt;");
			    break;
			case '&':
			    strTxt.append("&amp;");
			    break;
			default:
			    strTxt.append(chrBuff);
			}
		    }

		    rtnStr = strTxt.toString();

		} catch (Exception e) {
		    //e.printStackTrace();
		    throw new RuntimeException(e);
		}

		return rtnStr;
    }

    /**
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     *
     * @return Timestamp 값
     * @exception exception
     */
    public static String getTimeStamp() {

		String rtnStr = null;


		String pattern = "yyyyMMddhhmmssSSS";

		try {
		    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		    Timestamp ts = new Timestamp(System.currentTimeMillis());

		    rtnStr = sdfCurrent.format(ts.getTime());
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}

		return rtnStr;
    }

    /**
     * html의 특수문자를 표현하기 위해
     *
     * @param srcString
     * @return String
     * @exception Exception
     */
    public static String getHtmlStrCnvr(String srcString) {

    	String tmpString = srcString;

		try
		{
			tmpString = tmpString.replaceAll("&lt;", "<");
			tmpString = tmpString.replaceAll("&gt;", ">");
			tmpString = tmpString.replaceAll("&amp;", "&");
			tmpString = tmpString.replaceAll("&nbsp;", " ");
			tmpString = tmpString.replaceAll("&apos;", "\'");
			tmpString = tmpString.replaceAll("&quot;", "\"");
		}
		catch (Exception ex)
		{
		    throw new RuntimeException(ex);
		}

		return  tmpString;

	}

    /**
     * <p>날짜 형식의 문자열 내부에 마이너스 character(-)를 추가한다.</p>
     *
     * <pre>
     *   StringUtil.addMinusChar("20100901") = "2010-09-01"
     * </pre>
     *
     * @param date  입력받는 문자열
     * @return " - "가 추가된 입력문자열
     */
	public static String addMinusChar(String date) {
		if(date.length() == 8)
		    return date.substring(0,4).concat("-").concat(date.substring(4,6)).concat("-").concat(date.substring(6,8));
		else return "";
	}


	   /**
     * 다운로드 할 파일명 인코딩
     * @param filename
     * @return
     * @throws Exception
     */
	public static String encodeFileName(String filename) throws Exception {

    	String fileName = "";

    	fileName = URLEncoder.encode(filename, "UTF-8");

    	return fileName;
    }

	/**
	 * 첫글자만 대문자로 변환
	 * @param data
	 * @return
	 */
	public static String stringFirstUpper(String data) {

		String transStr = data.substring(0,1).toUpperCase();
		transStr += data.substring(1);

		return transStr;
	}

	/**
	 * IndexOfLastCrLf
	 * @param str
	 * @return
	 */
	public static int IndexOfLastCrLf(String str)
	{
	    char    Cr = '\r';
	    char    Lf = '\n';
	    int     nFound = 0;

	    nFound = str.lastIndexOf(Lf);
	    if(nFound > -1) {
	    	nFound = str.indexOf(Cr);
	    }
	    return nFound;
	}

	/**
	 * IndexOfCrLf
	 * @param str
	 * @return
	 */
	public static int IndexOfCrLf(String str)
	{
	    char    Cr = '\r';
	    char    Lf = '\n';
	    int     nFound = 0;

	    nFound = str.lastIndexOf(Cr);
	    if(nFound > -1) {
	    	nFound = str.indexOf(Lf);
	    }
	    return nFound;
	}

	/**
	 * isDigits
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDigits(String str)	{
	    int     nDecCnt = 0;

	    if (str == null) {
			return false;
		}
		int strSize = str.length();
		if (strSize == 0) return false;

	    char [] tmpChrArr = str.toCharArray();

	    for(int i = 0; i < strSize; i++) {

	        if((tmpChrArr[i] >= '0') && (tmpChrArr[i] <= '9')) {
	            // Normar Number
	        } else if(tmpChrArr[i] == '.')
	        {
	            // Decimal Point
	            nDecCnt++;
	        } else  {
	            return false;
	        }
	    }
	    if(nDecCnt > 1) {
	        return false;
	    }
	    return true;
	}

	/**
	 * isAlphabetic
	 *
	 * @param str
	 * @return
	 */
	public static boolean isAlphabetic(String str) {

		if (str == null) {
			return false;
		}
		int strSize = str.length();
		if (strSize == 0) return false;

	    char [] tmpChrArr = str.toCharArray();

	    for(int i = 0; i < strSize; i++)
	    {
	        if(((tmpChrArr[i] >= 'A') && (tmpChrArr[i] <= 'Z')) || ((tmpChrArr[i] >= 'a') && (tmpChrArr[i] <= 'z'))) {
	        } else   {
	            return false;
	        }
	    }
	    return true;
	}
	/**
	 * isSpace
	 *
	 * @param str
	 * @return
	 */
	public static boolean isSpace(String str) {

		if (str == null) {
			return false;
		}
		int strSize = str.length();
		if (strSize == 0) return false;

	    char [] tmpChrArr = str.toCharArray();

	    for(int i = 0; i < strSize; i++)
	    {
	        if(tmpChrArr[i] != ' ')
	        	return false;
	    }

	    return true;
	}

	public static int indexOfCharArray(char [] strArr, char chr) {
		String str = String.valueOf(strArr);

		if (str == null) {
			return -1;
		}
		int strSize = str.length();
		if (strSize == 0) return -1;

		return str.indexOf(chr);
	}

    /**
     * 특정 알파벳을 기준으로 다음 알파벳 구하기
     * Z  --> AA
     * :
     * AZ --> BA
     * :
     * ZZ --> AAA
     * :
     * @param source
     * @return
     */
    public static String nextAlphabet(String source) {
        int length = source.length();
        char lastChar = source.charAt(length - 1);
        if (lastChar == 'Z') {
          if (length == 1) {
            return source = "AA";
          }
          source = nextAlphabet(source.substring(0, length - 1));
          source += "A";
          return source;
        }
        return source.substring(0, length - 1) + String.valueOf((char) (lastChar + 1));
      }

    /**
     * 문자열을 UTF-8로 변환한다.
     * 검색조건에 다국어로 검색하는 경우에 사용
     * @param str
     * @return
     */
    public static String toUTF8(String str) throws UnsupportedEncodingException {
    	return java.net.URLDecoder.decode(str, "UTF-8");
    }
}