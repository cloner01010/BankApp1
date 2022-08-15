package bank;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrivateBankDeserializer implements JsonDeserializer<List<Transaction>> {
    public List<Transaction> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonArray ja = json.getAsJsonArray();
        List<Transaction> transactions = new ArrayList<>();
        for(JsonElement jsonElement: ja){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String classname = jsonObject.get("CLASSNAME").getAsString();
            JsonObject classValue = jsonObject.get("INSTANCE").getAsJsonObject();
            String date=classValue.get("date").getAsString();
            double amount =  classValue.get("amount").getAsDouble();
            String description =classValue.get("description").getAsString();
            switch (classname) {
                case "Payment" -> {
                    Payment payment = new Payment(
                            date, amount, description,
                            classValue.get("incomingInterest").getAsDouble(),
                            classValue.get("outgoingInterest").getAsDouble()

                    );
                    transactions.add(payment);
                }
                case "IncomingTransfer" -> {
                    IncomingTransfer incomingTransfer = new IncomingTransfer(date, amount, description,
                            classValue.get("sender").getAsString(),
                            classValue.get("recipient").getAsString()

                    );
                    transactions.add(incomingTransfer);
                }
                case "OutgoingTransfer" -> {
                    OutgoingTransfer outgoingTransfer = new OutgoingTransfer(date, amount, description,
                            classValue.get("sender").getAsString(),
                            classValue.get("recipient").getAsString()

                    );
                    transactions.add(outgoingTransfer);
                }
            }
        }
        return transactions;
    }
}
