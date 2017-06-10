package Models;

/**
 * Created by 1405214 on 10-06-2017.
 */

public class Order extends Product {


    public String Tracking_ID;
    public String rating;
    public String Time_Order;
    public Order()
    {

    }
    public Order(String Category,String Description,String X150,String X600,String Price,String Product_Id,String Tracking_ID,String rating,String Time_Order)
    {
        super(Category,Description,X150,X600,Price,Product_Id);
        this.Tracking_ID=Tracking_ID;
        this.rating=rating;
        this.Time_Order=Time_Order;
    }

}
