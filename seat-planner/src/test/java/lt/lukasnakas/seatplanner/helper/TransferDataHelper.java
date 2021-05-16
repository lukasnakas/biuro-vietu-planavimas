package lt.lukasnakas.seatplanner.helper;

import lt.lukasnakas.seatplanner.model.Transfer;

import java.util.ArrayList;
import java.util.List;

public class TransferDataHelper {


    public static Transfer getTransfer() {
        return new Transfer("TeamA", "6.02", 5);
    }

    public static List<Transfer> getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(getTransfer());
        return transfers;
    }
}
