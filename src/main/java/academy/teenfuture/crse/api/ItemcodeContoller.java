package academy.teenfuture.crse.api;

import academy.teenfuture.crse.modal.ItemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequestMapping("japi/wung/quasar2/itemCode")
@RestController
public class ItemcodeContoller {
    HashMap<String, ItemCode> DB = new HashMap<>();

    @Autowired
    public ItemcodeContoller() {
        for (int i = 0; i < 100; i++) {
            ItemCode itemCode = new ItemCode();
            itemCode.setQty((int) (Math.random() * 1000));
            itemCode.setDescription("ItemCode " + i + " Description");
            DB.put(String.format("%09d", i), itemCode);
        }
    }

    @GetMapping
    public ResponseEntity<?> getItemCode() {
        return ResponseEntity.ok(DB.keySet());
    }

    @GetMapping("/{itemCode}")
    public ResponseEntity<?> getItemCode(@PathVariable String itemCode) {
        if (!DB.containsKey(itemCode))
            return ResponseEntity.badRequest().body("ItemCode " + itemCode + " is not Found");
        return ResponseEntity.ok(DB.get(itemCode));
    }
}
