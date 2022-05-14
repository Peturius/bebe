import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

    Window window = new Window();

    }

}

class Window {

    public Window() {

        Database database = new Database();
        Scanner scanner = new Scanner(System.in);

        showMenu();
        int choose = -1;
        try {
            choose = scanner.nextInt();
        }catch (Exception e) {
            System.out.println("Error happened");
            Window window = new Window();
        }


        while (choose != 5) {

            if(choose == 1) {
                System.out.println("Trader");
                String trader = scanner.next();
                System.out.println("Price amount");
                Integer priceAmount = scanner.nextInt();
                System.out.println("Price item");
                String priceItem = scanner.next();
                System.out.println("Reward amount");
                Integer rewardAmount = scanner.nextInt();
                System.out.println("Reward item");
                String rewardItem = scanner.next();
                database.addNewThing(trader, priceItem, rewardItem, priceAmount, rewardAmount);
            }
            else if(choose == 2) {
                try {
                    System.out.println("Remove offer by ID");
                    Integer tradeId = scanner.nextInt();
                    if (tradeId >= 0 && tradeId < database.dataArray.size()){
                        database.removeThing(tradeId);
                    }

                } catch (Exception e) {
                    System.out.println("Error happened");
                    Window window = new Window();
                }

            }
            else if(choose == 3) {

                try {
                    System.out.println("Trade ID you want to modify");
                    Integer tradeId = scanner.nextInt();
                    System.out.println("Trader");
                    String trader = scanner.next();
                    System.out.println("Price amount");
                    Integer priceAmount = scanner.nextInt();
                    System.out.println("Price item");
                    String priceItem = scanner.next();
                    System.out.println("Reward amount");
                    Integer rewardAmount = scanner.nextInt();
                    System.out.println("Reward Item");
                    String rewardItem = scanner.next();
                    database.modifyThing(trader, tradeId, priceItem, rewardItem, priceAmount, rewardAmount);
                } catch (Exception e) {
                    System.out.println("Error happened");
                    Window window = new Window();
                }
            }
            else if(choose == 4) {
                database.showThings();
            }

            showMenu();
            try {
                choose = scanner.nextInt();
            }catch (Exception e) {
                System.out.println("Error happened");
                Window window = new Window();
            }
        }
        database.saveToXml();
    }

    public void showMenu() {
        System.out.println();
        System.out.println("Welcome to the trading hub!");
        System.out.println("Choose by numbers...");
        System.out.println("Add new offer: 1");
        System.out.println("Remove offer: 2");
        System.out.println("Modify offer: 3");
        System.out.println("Show all offers: 4");
        System.out.println("EXIT: 5");
    }

}
class Database {

    ArrayList<Thing> dataArray = new ArrayList<>();
    public Database() { //TODO ITT LESZ AZ XML CUCCOS

        try {

            File inputFile = new File("src/things2.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("offers");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.print(nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String traderX = eElement.getAttribute("name");

                    NodeList trades = eElement.getElementsByTagName("trade");

                        for (int temp2 = 0; temp2 < trades.getLength(); temp2++) {
                            Thing thing = new Thing();
                            Node nTrade = trades.item(temp2);

                            Node nTrades = nTrade.getParentNode();
                            if(nTrades.getNodeType() == Node.ELEMENT_NODE) {
                                Element eTrades = (Element) nTrades;
                                //System.out.println(eTrades.getAttribute("name"));
                                thing.trader = eTrades.getAttribute("name");
                            }


                            if(nTrade.getNodeType() == Node.ELEMENT_NODE) {
                                Element eTrade = (Element) nTrade;

                                NodeList priceItem = eTrade.getElementsByTagName("priceitem");
                                NodeList priceAmount = eTrade.getElementsByTagName("priceamount");
                                NodeList rewardItem = eTrade.getElementsByTagName("rewarditem");
                                NodeList rewardAmount = eTrade.getElementsByTagName("rewardamount");

                                for (int temp3 = 0; temp3 < priceItem.getLength(); temp3++) {
                                    Node nPriceItem = priceItem.item(temp3);
                                    if (nPriceItem.getNodeType() == Node.ELEMENT_NODE) {
                                        Element ePriceItem = (Element) nPriceItem;
                                        String priceItemX = ePriceItem.getTextContent();
                                        thing.priceItem = priceItemX;
                                    }
                                }
                                for (int temp3 = 0; temp3 < priceAmount.getLength(); temp3++) {
                                    Node nPriceAmount = priceAmount.item(temp3);
                                    if (nPriceAmount.getNodeType() == Node.ELEMENT_NODE) {
                                        Element ePriceAmount = (Element) nPriceAmount;
                                        String priceAmountX = ePriceAmount.getTextContent();
                                        thing.priceAmount = Integer.valueOf(priceAmountX);
                                    }
                                }
                                for (int temp3 = 0; temp3 < rewardItem.getLength(); temp3++) {
                                    Node nRewardItem = rewardItem.item(temp3);
                                    if (nRewardItem.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eRewardItem = (Element) nRewardItem;
                                        String RewardItemX = eRewardItem.getTextContent();
                                        thing.rewardItem = RewardItemX;
                                    }
                                }
                                for (int temp3 = 0; temp3 < rewardAmount.getLength(); temp3++) {
                                    Node nRewardAmount = rewardAmount.item(temp3);
                                    if (nRewardAmount.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eRewardAmount = (Element) nRewardAmount;
                                        String rewardAmountX = eRewardAmount.getTextContent();
                                        thing.rewardAmount = Integer.valueOf(rewardAmountX);
                                    }
                                }
                                thing.tradeId = dataArray.size();
                                dataArray.add(thing);
                            }


                        }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewThing(String traderIn, String priceItemIn, String rewardItemIn, Integer priceAmountIn, Integer rewardAmountIn) {
        Thing thing = new Thing();
        boolean vanE = false;

        for (int i = 0; i < dataArray.size(); i++) {
            if (dataArray.get(i).priceAmount == priceAmountIn &&
                    dataArray.get(i).rewardAmount == rewardAmountIn &&
                    dataArray.get(i).rewardItem.equals(priceItemIn) &&
                    dataArray.get(i).trader.equals(traderIn) &&
                    dataArray.get(i).priceItem.equals(rewardItemIn)) {
                vanE = true;
            }
        }
        if (!vanE) {
            thing.trader = traderIn;
            thing.priceAmount = priceAmountIn;
            thing.priceItem = priceItemIn;
            thing.rewardAmount = rewardAmountIn;
            thing.rewardItem = rewardItemIn;
            thing.tradeId = dataArray.size();
        }
        dataArray.add(thing);
    }

    public void removeThing(Integer tradeIdIn) {
        for (int i = 0; i < dataArray.size(); i++) {
            if (dataArray.get(i).tradeId == tradeIdIn) {
                dataArray.remove(i);
            }
        }
        for (int i = 0; i < dataArray.size() ; i++) {
            dataArray.get(i).tradeId = i;
        }
    }

    public void modifyThing (String traderIn, Integer tradeIdIn, String priceItemIn, String rewardItemIn, Integer priceAmountIn, Integer rewardAmountIn) {

        boolean lehetE = false;

        for (int i = 0; i < dataArray.size(); i++) {
            if (dataArray.get(i).tradeId == tradeIdIn) {
                lehetE = true;
            }
        }
        for (int i = 0; i < dataArray.size(); i++) {
            if (dataArray.get(i).priceAmount == priceAmountIn &&
                    dataArray.get(i).rewardAmount == rewardAmountIn &&
                    dataArray.get(i).rewardItem.equals(priceItemIn) &&
                    dataArray.get(i).trader.equals(traderIn) &&
                    dataArray.get(i).priceItem.equals(rewardItemIn)) {
                lehetE = false;
            }
        }
        if(lehetE) {
            dataArray.get(tradeIdIn).rewardItem = rewardItemIn;
            dataArray.get(tradeIdIn).rewardAmount = rewardAmountIn;
            dataArray.get(tradeIdIn).priceItem = priceItemIn;
            dataArray.get(tradeIdIn).priceAmount = priceAmountIn;
        }
    }

    public void showThings () {
        System.out.println();

        for (int i = 0; i < dataArray.size(); i++) {
            System.out.printf("Trader: " + dataArray.get(i).trader + "\r\n");
            System.out.printf("Trade ID: " + dataArray.get(i).tradeId + "\r\n");
            System.out.printf("You give: " + dataArray.get(i).priceAmount + " " + dataArray.get(i).priceItem + "\r\n");
            System.out.printf("You get:  " + dataArray.get(i).rewardAmount + " " + dataArray.get(i).rewardItem + "\r\n\n");
        }
    }

    public void saveToXml () {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("offers");
            doc.appendChild(rootElement);

            for (int i = 0; i < dataArray.size(); i++) {

                Element trader = doc.createElement("trader");
                rootElement.appendChild(trader);

                Attr attr = doc.createAttribute("name");
                attr.setValue(dataArray.get(i).trader);
                trader.setAttributeNode(attr);

                Element trade = doc.createElement("trade");
                trader.appendChild(trade);

                Attr attr1 = doc.createAttribute("id");
                attr1.setValue(dataArray.get(i).tradeId.toString());
                trade.setAttributeNode(attr1);

                Element priceAmount = doc.createElement("priceamount");
                trade.appendChild(priceAmount);
                priceAmount.appendChild(doc.createTextNode(dataArray.get(i).priceAmount.toString()));

                Element priceItem = doc.createElement("priceitem");
                trade.appendChild(priceItem);
                priceItem.appendChild(doc.createTextNode(dataArray.get(i).priceItem.toString()));

                Element rewardAmount = doc.createElement("rewardamount");
                trade.appendChild(rewardAmount);
                rewardAmount.appendChild(doc.createTextNode(dataArray.get(i).rewardAmount.toString()));

                Element rewardItem = doc.createElement("rewarditem");
                trade.appendChild(rewardItem);
                rewardItem.appendChild(doc.createTextNode(dataArray.get(i).rewardItem.toString()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/things2.xml"));
            transformer.transform(source, result);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class Thing {
    String trader;
    Integer tradeId;
    String priceItem;
    String rewardItem;
    Integer priceAmount;
    Integer rewardAmount;
}