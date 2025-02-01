package cz.cvut.fel.omo.semestralka.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StorageMoneyTransaction {

    public static List<MoneyTransaction> transactionHistory = new ArrayList<>();

}
