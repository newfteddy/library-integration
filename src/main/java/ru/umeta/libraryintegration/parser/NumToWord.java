package ru.umeta.libraryintegration.parser;

/**
 * Created by danielfeldman on 17.03.17.
 */
public class NumToWord {

    public static final String unitsMap[] = { "ноль", "один", "два", "три", "четыре", "пять","шесть", "семь", "восемь", "девять", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнацать" };
    public static final String tensMap[] = { "ноль", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто" };

    public NumToWord() {

    }

    public String ConvertNumToWord (int number){
        if (number == 0)
            return "ноль";

        if (number < 0)
            return "минус " + ConvertNumToWord(Math.abs(number));

        String words = "";

        if ((number / 1000000000) > 0)
        {
            words += ConvertNumToWord(number / 1000000000) + " миллиард ";
            number %= 1000000000;
        }

        if ((number / 1000000) > 0)
        {
            words += ConvertNumToWord(number / 1000000) + " миллион ";
            number %= 1000000;
        }

        if ((number / 1000) > 0)
        {
            words += ConvertNumToWord(number / 1000) + " тысяча ";
            number %= 1000;
        }

        if ((number / 100) > 0)
        {
            words += ConvertNumToWord(number / 100) + " сот ";
            number %= 100;
        }

        if (number > 0)
        {
            if (number < 20)
                words += unitsMap[number];
            else
            {
                words += tensMap[number / 10];
                if ((number % 10) > 0)
                    words += "-" + unitsMap[number % 10];
            }
        }

        return words;
    }
}
