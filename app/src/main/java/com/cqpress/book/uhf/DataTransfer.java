package com.cqpress.book.uhf;

/**
 * tool class
 * 
 */
public class DataTransfer
{
	//累积异或
	public static int accumulation(char[] chars) {
		int result = 0;
		for (int i = 0; i < chars.length; i++) {
			result ^= Integer.parseInt("" + chars[i], 16);
		}
		return result;
	}

	public static String xGetString(byte[] bs)
	{
		if (bs != null)
		{
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < bs.length; i++)
			{
//				sBuffer.append(String.format("%02x ", bs[i])); old 通过空格分隔
				sBuffer.append(String.format("%02x", bs[i]));
			}
			return sBuffer.toString();
		}
		return "null";
	}

	public static byte[] getBytesByHexString(String string)
	{
		string = string.replaceAll(" ", "");// delete spaces
		int len = string.length();
		if (len % 2 == 1)
		{
			return null;
		}
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = (byte) (Integer.valueOf(string.substring((i * 2), (i * 2 + 2)), 16) & 0xff);
		}
		return ret;
	}

}
