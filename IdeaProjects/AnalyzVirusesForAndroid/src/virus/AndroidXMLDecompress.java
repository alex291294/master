package virus;

/**
 * Created by alex on 19.01.16.
 */
class AndroidXMLDecompress {

    private static int endDocTag = 0x00100101;
    private static int startTag = 0x00100102;
    private static int endTag = 0x00100103;

    private static void prt(String str) {
        System.err.print(str);
    }

    public static String decompressXML(byte[] xml) {

        StringBuilder finalXML = new StringBuilder();
        int numbStrings = LEW(xml, 4 * 4);
        int sitOff = 0x24;
        int stOff = sitOff + numbStrings * 4;
        int xmlTagOff = LEW(xml, 3 * 4);
        for (int ii = xmlTagOff; ii < xml.length - 4; ii += 4) {
            if (LEW(xml, ii) == startTag) {
                xmlTagOff = ii;
                break;
            }
        }
        int off = xmlTagOff;
        int indent = 0;
        int startTagLineNo = -2;
        while (off < xml.length) {
            int tag0 = LEW(xml, off);
            int lineNo = LEW(xml, off + 2 * 4);
            int nameSi = LEW(xml, off + 5 * 4);

            if (tag0 == startTag) {
                int numbAttrs = LEW(xml, off + 7 * 4);
                off += 9 * 4;
                String name = compXmlString(xml, sitOff, stOff, nameSi);
                startTagLineNo = lineNo;
                StringBuffer sb = new StringBuffer();
                for (int ii = 0; ii < numbAttrs; ii++) {
                    int attrNameSi = LEW(xml, off + 4);
                    int attrValueSi = LEW(xml, off + 2 * 4);
                    int attrResId = LEW(xml, off + 4 * 4);
                    off += 5 * 4;
                    String attrName = compXmlString(xml, sitOff, stOff,
                            attrNameSi);
                    String attrValue = null;
                    if (attrValueSi != -1) {
                        attrValue = compXmlString(xml, sitOff, stOff, attrValueSi);
                    } else {
                        if (attrResId == -1)
                            attrValue = "resourceID 0x" + Integer.toHexString(attrResId);
                        else
                            attrValue = String.valueOf(Integer.valueOf(Integer.toHexString(attrResId), 16).intValue());
                    }
                    sb.append(" " + attrName + "=\"" + attrValue + "\"");
                }
                finalXML.append("<" + name + sb + ">");
                prtIndent(indent, "<" + name + sb + ">");
                indent++;

            } else if (tag0 == endTag) {
                indent--;
                off += 6 * 4;
                String name = compXmlString(xml, sitOff, stOff, nameSi);
                finalXML.append("</" + name + ">");
                prtIndent(indent, "</" + name + "> (line " + startTagLineNo
                        + "-" + lineNo + ")");
            } else if (tag0 == endDocTag) {
                break;
            } else {
                prt("  Unrecognized tag code '" + Integer.toHexString(tag0)
                        + "' at offset " + off);
                break;
            }
        }
        return finalXML.toString();
    }

    private static String compXmlString(byte[] xml, int sitOff, int stOff, int strInd) {
        if (strInd < 0)
            return null;
        int strOff = stOff + LEW(xml, sitOff + strInd * 4);
        return compXmlStringAt(xml, strOff);
    }

    private static String spaces = "                                             ";

    private static void prtIndent(int indent, String str) {
        prt(spaces.substring(0, Math.min(indent * 2, spaces.length())) + str);
    }

    private static String compXmlStringAt(byte[] arr, int strOff) {
        int strLen = arr[strOff + 1] << 8 & 0xff00 | arr[strOff] & 0xff;
        byte[] chars = new byte[strLen];
        for (int ii = 0; ii < strLen; ii++) {
            chars[ii] = arr[strOff + 2 + ii * 2];
        }
        return new String(chars);
    }

    private static int LEW(byte[] arr, int off) {
        return arr[off + 3] << 24 & 0xff000000 | arr[off + 2] << 16 & 0xff0000 | arr[off + 1] << 8 & 0xff00 | arr[off] & 0xFF;
    }
}