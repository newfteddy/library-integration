package ru.umeta.rnbharvester;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Ad-hoc solution to extracting data from rnd dump
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 14.10.2015.
 */
public class ModsCreator {

    private final static String SQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final static String SQL_DB_CONNECT_STRING = "jdbc:mysql://localhost:3306/";
    private final static String SQL_DB_NAME = "library";
    private final static String SQL_DB_USER = "daniel";
    private final static String SQL_DB_PASS = "260697";
    private static final long FOLDER_BATCH_SIZE = 100;
    private static final long BATCH_SIZE = 10000;

    private static boolean driverLoaded = false;

    static {
        try {
            Class.forName(SQL_DRIVER_NAME);
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println(SQL_DRIVER_NAME + " driver failed to load");
        }
    }

    private static void getRecordListFromDB(long startId) throws SQLException, IOException, ParserConfigurationException, TransformerException {
        Connection conn = DriverManager.getConnection(SQL_DB_CONNECT_STRING + SQL_DB_NAME, SQL_DB_USER, SQL_DB_PASS);
        PreparedStatement statement = conn.prepareStatement(
                "SELECT Id, Author, Name, SubName, PublishYear, ISBN " +
                        "FROM `tbl_common_biblio_card` " +
                        "WHERE id%100 = 3 AND Author IS NOT NULL AND Id >= ? AND Id < ? + " + BATCH_SIZE);

        statement.setLong(1, startId);
        statement.setLong(2, startId);
        ResultSet result = statement.executeQuery();
        List<String> recordList = new ArrayList<>();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("modsCollection");
        rootElement.setAttribute("xsi:schemaLocation", "http://www.loc.gov/mods/v3 http://www.loc" +
                ".gov/standards/mods/v3/mods-3-5.xsd");
        rootElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xmlns","http://www.loc.gov/mods/v3");
        doc.appendChild(rootElement);

        while (result.next()) {
            String author = result.getString(2);
            author = author != null ? author : "";
            String title = result.getString(3);
            title = title != null ? title : "";
            String subTitle = result.getString(4);
            subTitle = subTitle != null ? subTitle : "";
            String publishYear = result.getString(5);
            publishYear = publishYear != null ? publishYear : "";
            String isbn = result.getString(6);
            isbn = isbn != null ? isbn : "";

            Element mods = doc.createElement("mods");
            mods.setAttribute("version", "3.5");
            rootElement.appendChild(mods);

            Element titleInfo = doc.createElement("titleInfo");
            mods.appendChild(titleInfo);

            Element titleNode = doc.createElement("title");
            titleNode.appendChild(doc.createTextNode(title));
            titleInfo.appendChild(titleNode);
            Element subTitleNode = doc.createElement("subTitle");
            subTitleNode.appendChild(doc.createTextNode(subTitle));
            titleInfo.appendChild(subTitleNode);

            Element name = doc.createElement("name");
            name.setAttribute("type", "personal");
            name.setAttribute("usage", "primary");
            name.setAttribute("nameTitleGroup", "1");
            mods.appendChild(name);
            Element namePart = doc.createElement("namePart");
            namePart.appendChild(doc.createTextNode(author));
            name.appendChild(namePart);

            Element originInfo = doc.createElement("originInfo");
            mods.appendChild(originInfo);
            Element dateIssued = doc.createElement("dateIssued");
            dateIssued.appendChild(doc.createTextNode(publishYear));
            originInfo.appendChild(dateIssued);

            Element identifier = doc.createElement("identifier");
            identifier.setAttribute("type", "isbn");
            identifier.appendChild(doc.createTextNode(isbn));
            mods.appendChild(identifier);
        }

        String folder = isMkdirs(startId);
        File file = new File(folder + startId + ".xml");
        file.createNewFile();


        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultStream = new StreamResult(file);

        transformer.transform(source, resultStream);

        conn.close();
    }

    private static String isMkdirs(long startId) {
        String folder = "results/" + startId / 100000 + "/";
        new File(folder).mkdirs();
        return folder;
    }

    public static void main(String[] args) {
        if (driverLoaded) {
            try {
                long minId = getRecordMinIdFromDB();
                long maxId = Math.min(4282903,getRecordMaxIdFromDB());
                for (long curId = minId; curId < maxId; curId += BATCH_SIZE) {
                    getRecordListFromDB(curId);
                }
            } catch (Exception e) {
                System.err.println("Internal Error");
                e.printStackTrace();
            }

        }
    }

    private static long getRecordMinIdFromDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(SQL_DB_CONNECT_STRING + SQL_DB_NAME, SQL_DB_USER, SQL_DB_PASS)) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT Id FROM `tbl_common_biblio_card` ORDER BY Id ASC LIMIT 1");
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getLong(1);
        }
    }

    private static long getRecordMaxIdFromDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(SQL_DB_CONNECT_STRING + SQL_DB_NAME, SQL_DB_USER, SQL_DB_PASS)) {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT Id FROM `tbl_common_biblio_card` ORDER BY Id DESC LIMIT 1");
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getLong(1);
        }
    }

}
