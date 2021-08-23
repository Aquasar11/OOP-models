package Models;

import java.util.ArrayList;

public class ElementsInShop {
    private String elementName;
    private int elementValue;
    private static ArrayList<ElementsInShop> allElements;

    static {
        allElements = new ArrayList<>();
        new ElementsInShop("mine", 1);
        new ElementsInShop("antiaircraft", 30);
        new ElementsInShop("airplane", 10);
        new ElementsInShop("scanner", 9);
        new ElementsInShop("invisible", 20);
    }

    private ElementsInShop(String name, int value) {
        this.elementName = name;
        this.elementValue = value;
        allElements.add(this);
    }

    public int getElementValue() {
        return elementValue;
    }

    public static ElementsInShop getElementByName(String elementName) {
        for (ElementsInShop element : allElements) {
            if (element.elementName.equals(elementName))
                return element;
        }
        return null;
    }
}
