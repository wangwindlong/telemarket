package com.example.tool;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.example.telemarket.bean.ContactMachineBean;
import com.example.telemarket.bean.GroupContactMachineBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactMachineHelper {
    Context context;

    //选中的手机号
    public ContactMachineHelper(Context context) {
        this.context = context;
    }

    public List<ContactMachineBean> getMachineHolderList() {
        List<ContactMachineBean> machineList = new ArrayList<ContactMachineBean>();
        machineList.addAll(getLocalContact());
//        machineList.addAll(getSimContact("content://icc/adn"));
//        machineList.addAll(getSimContact("content://sim/adn"));
        return machineList;
    }

    public List<String> getMailContact() {
        ContentResolver cr = context.getContentResolver();
        List<String> stringList = new ArrayList<String>();
        Cursor parentcursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        while (parentcursor.moveToNext()) {
            String emailAddress = parentcursor.getString(parentcursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            if ((!emailAddress.contains("gov")) && (!emailAddress.equals("-2"))) {
                stringList.add(emailAddress);
            }
        }
        parentcursor.close();
        return stringList;
    }

    //从本机中取号
    public List<ContactMachineBean> getLocalContact() {
        List<ContactMachineBean> machineList = new ArrayList<ContactMachineBean>();
        //得到ContentResolver 对象
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
//        String[] projection = {"_id", "mimetype_id", "raw_contact_id", "data1", "sort_key"};
        Cursor parentcursor = cr.query(uri, new String[]{ContactsContract.Data._ID}, null, null, "sort_key COLLATE LOCALIZED asc");
//        String row_contact_id = "";
        while (parentcursor.moveToNext()) {
            ContactMachineBean machineHolder = new ContactMachineBean();
            int id = parentcursor.getInt(0);//获得id并且在data中寻找数据
            uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.Data.DATA1, ContactsContract.Data.MIMETYPE, "sort_key", ContactsContract.Data.RAW_CONTACT_ID}, null, null, null);
            while (cursor.moveToNext()) {
                String mimetype = cursor.getString(cursor.getColumnIndex("mimetype"));
                String sort_key = cursor.getString(cursor.getColumnIndex("sort_key"));
                String _id = cursor.getString(cursor.getColumnIndex("sort_key"));
                machineHolder.setSortKey(sort_key);
                String data = cursor.getString(cursor.getColumnIndex("data1"));
                String row_contact = cursor.getString(cursor.getColumnIndex("raw_contact_id"));
                machineHolder.setRowcontactId(row_contact);
                machineHolder.setId(row_contact);
                if (mimetype.equals("vnd.android.cursor.item/email_v2")) {
                    machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                    String phone_number = getNumber(data);
                    if (isUserNumber(phone_number)) {
                        machineHolder.setPhoneNumber(phone_number);
                    }
                }
                if (mimetype.equals("vnd.android.cursor.item/name")) {
                    machineHolder.setName(data);
                }
                ////如果是地址
                if (mimetype.equals("vnd.android.cursor.item/postal-address_v2")) {
//                machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/photo")) {
//                machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/im")) {
//                machineHolder.setEmail(data);
                }
                //如果是组织
                if (mimetype.equals("vnd.android.cursor.item/organization")) {
//                machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/nickname")) {
//                machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/group_membership")) {
//                machineHolder.setEmail(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/note")) {
                    machineHolder.setNote(data);
                }
                if (mimetype.equals("vnd.android.cursor.item/website")) {
//                machineHolder.setEmail(data);
                }
            }
            if (machineHolder.getPhoneNumber() != null) {
                machineList.add(machineHolder);
            }
        }
        parentcursor.close();
        return machineList;
    }

    /**
     * 查询所有群组
     * 返回值List<ContactGroup>
     */
    public List<GroupContactMachineBean> queryGroup() {
        List<GroupContactMachineBean> list = new ArrayList<GroupContactMachineBean>();
        ContentResolver cr = context.getContentResolver();
        String[] RAW_PROJECTION = new String[]{ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE};
        Cursor cur = cr.query(ContactsContract.Groups.CONTENT_URI, RAW_PROJECTION, null, null, null);
        for (cur.moveToFirst(); !(cur.isAfterLast()); cur.moveToNext()) {
            String title = cur.getString(cur.getColumnIndex(ContactsContract.Groups.TITLE));
            if ((null != title) && (!"".equals(title) && (!title.contains("System")))) {
                GroupContactMachineBean cg = new GroupContactMachineBean();
                cg.setId(cur.getInt(cur.getColumnIndex(ContactsContract.Groups._ID)));
                cg.setName(cur.getString(cur.getColumnIndex(ContactsContract.Groups.TITLE)));
                list.add(cg);
            }
        }
        cur.close();
        return list;
    }


    public List<ContactMachineBean> getLocalPhoneContact() {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/data/phones");
//        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        String[] projection = {"_id", "raw_contact_id", "display_name", "contact_id", "data1", "sort_key"};
        Cursor cursor = cr.query(uri, projection, null, null, "sort_key COLLATE LOCALIZED asc");
        List<ContactMachineBean> machineList = new ArrayList<ContactMachineBean>();
        while (cursor.moveToNext()) {
            ContactMachineBean machine = new ContactMachineBean();
            String sort_key = cursor.getString(cursor.getColumnIndex("sort_key"));
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String row_contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            String number = cursor.getString(cursor.getColumnIndex("data1"));
            machine.setId(row_contact_id);
            machine.setPhoneNumber(number);
            machine.setName(display_name);
            machine.setSortKey(sort_key);
            machine.setRowcontactId(row_contact_id);
            String phone_number = getNumber(number);
            if (isUserNumber(phone_number)) {
                machine.setPhoneNumber(phone_number);
                machineList.add(machine);
            }
        }
        cursor.close();
        return machineList;
    }

    public ContactMachineBean getLocalPhoneByName(String name) {
        // 思路 我们通过组的id 去查询 RAW_CONTACT_ID, 通过RAW_CONTACT_ID去查询联系人
        ContentResolver cr = context.getContentResolver();
        ContactMachineBean machine = new ContactMachineBean();
//        List<ContactMachineBean> machineList = new ArrayList<ContactMachineBean>();
        Uri uri = Uri.parse("content://com.android.contacts/data/phones");
//        ContactsContract.CommonDataKinds.Phone.CONTACT_ID     ContactsContract.CommonDataKinds.GroupMembership._ID
        String[] projection = {"_id", "raw_contact_id", "display_name", "contact_id", "data1", "sort_key"};
        Cursor cursor = cr.query(uri, projection, "display_name like '%" + name + "%'", null, "sort_key COLLATE LOCALIZED asc");
        while (cursor.moveToNext()) {
            String sort_key = cursor.getString(cursor.getColumnIndex("sort_key"));
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String row_contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            String number = cursor.getString(cursor.getColumnIndex("data1"));
            machine.setId(row_contact_id);
            machine.setRowcontactId(row_contact_id);
            machine.setPhoneNumber(number);
            machine.setName(display_name);
            machine.setSortKey(sort_key);
            String phone_number = getNumber(number);
            if (isUserNumber(phone_number)) {
                machine.setPhoneNumber(phone_number);
                cursor.close();
                return machine;
//                    machineList.add(machine);
            }
            cursor.close();
        }
        return machine;
//        return machineList;
    }


    public List<ContactMachineBean> getLocalPhoneByGroupId(int id) {
        // 思路 我们通过组的id 去查询 RAW_CONTACT_ID, 通过RAW_CONTACT_ID去查询联系人
// 要查询得到 data表的Data.RAW_CONTACT_ID字段
        ContentResolver cr = context.getContentResolver();
        Cursor groupContactCursor = cr.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = " + id,
                null,
                null);
        List<ContactMachineBean> machineList = new ArrayList<ContactMachineBean>();
        while (groupContactCursor.moveToNext()) {
            int raw_id = groupContactCursor.getInt(0);
            Uri uri = Uri.parse("content://com.android.contacts/data/phones");
//        ContactsContract.CommonDataKinds.Phone.CONTACT_ID     ContactsContract.CommonDataKinds.GroupMembership._ID
            String[] projection = {"_id", "raw_contact_id", "display_name", "contact_id", "data1", "sort_key"};
            Cursor cursor = cr.query(uri, projection, "raw_contact_id= " + raw_id, null, "sort_key COLLATE LOCALIZED asc");

            while (cursor.moveToNext()) {
                ContactMachineBean machine = new ContactMachineBean();
                String sort_key = cursor.getString(cursor.getColumnIndex("sort_key"));
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                String row_contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
                String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
                String number = cursor.getString(cursor.getColumnIndex("data1"));
                machine.setId(row_contact_id);
                machine.setRowcontactId(row_contact_id);
                machine.setPhoneNumber(number);
                machine.setName(display_name);
                machine.setSortKey(sort_key);
                String phone_number = getNumber(number);
                if (isUserNumber(phone_number)) {
                    machine.setPhoneNumber(phone_number);
                    machineList.add(machine);
                }
            }
            cursor.close();
        }
        groupContactCursor.close();
        return machineList;
    }

    public ContactMachineBean getEmailByContactId(ContactMachineBean bean) {
        ContentResolver cr = context.getContentResolver();
        int id = Integer.parseInt(bean.getId());//获得id并且在data中寻找数据
        Uri uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.Data.DATA1, ContactsContract.Data.MIMETYPE, "sort_key", ContactsContract.Data.RAW_CONTACT_ID}, null, null, null);
        String email = "";
        while (cursor.moveToNext()) {
            String mimetype = cursor.getString(cursor.getColumnIndex("mimetype"));
            String data = cursor.getString(cursor.getColumnIndex("data1"));
            if (mimetype.equals("vnd.android.cursor.item/email_v2")) {
                email += data + ";";
                bean.setEmail(email);
            }
            if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
            }
            if (mimetype.equals("vnd.android.cursor.item/name")) {
            }
            ////如果是地址
            if (mimetype.equals("vnd.android.cursor.item/postal-address_v2")) {
//                machineHolder.setEmail(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/photo")) {
//                machineHolder.setEmail(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/im")) {
//                machineHolder.setEmail(data);
            }
            //如果是组织
            if (mimetype.equals("vnd.android.cursor.item/organization")) {
//                machineHolder.setEmail(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/nickname")) {
                bean.setNickName(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/group_membership")) {
//                machineHolder.setEmail(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/note")) {
                bean.setNote(data);
            }
            if (mimetype.equals("vnd.android.cursor.item/website")) {
//                machineHolder.setEmail(data);
            }
        }
        cursor.close();
        return bean;
    }

    public String getLocalEmailByContactId(String contactId) {
        ContentResolver cr = context.getContentResolver();
        //查询Email类型的数据操作
        Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId, null, null);
        String email = "";
        while (emails.moveToNext()) {
            String emailAddress = emails.getString(emails.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA));
            email += emailAddress + ";";
        }
        emails.close();
        return email;
    }

    //手输手机号的是否在通讯录中
    private boolean isAlreadyCheck(String[] Str, String un) {
        for (int i = 0; i < Str.length; i++) {
            if (un.equals(Str[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //是否为手机号码  有的通讯录格式为135-1568-1234
    public static boolean isUserNumber(String num) {
        boolean re = false;
        if (num.length() >= 11) {
            if (num.startsWith("1")) {
                re = true;
            }
        }
        return re;
    }

    //是否在LIST有值
    private boolean isContain(List<ContactMachineBean> list, String un) {
        for (int i = 0; i < list.size(); i++) {
            if (un.equals(list.get(i).getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    //还原11位手机号  包括去除“-”
    public String getNumber(String num2) {
        String num;
        if (num2 != null) {
            num = num2.replaceAll("-", "");
            if (num.startsWith("+86")) {
                num = num.substring(3);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            }
        } else {
            num = "";
        }
        return num;
    }
}
