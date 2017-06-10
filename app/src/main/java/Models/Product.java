package Models;

/**
 * Created by 1405214 on 07-06-2017.
 */

public class Product {

    public String Product_Category;
    public String Product_Description;
    public String ImageX150;
    public String ImageX600;
    public String Price;
    public String Product_Id;
    public Product()
    {

    }
    public Product(String Category,String Description,String X150,String X600,String Price,String Product_Id)
    {
        this.Product_Category=Category;
        this.Product_Description=Description;
        this.ImageX150=X150;
        this.ImageX600=X600;
        this.Price=Price;
        this.Product_Id=Product_Id;
    }
    @Override
    public String toString()
    {
        return this.Product_Category+":"+this.Product_Description;
    }

}
