Cash Register
========

Cash Register is an application that helps users manage grocery products and items to a Shopping Cart.

    import project into Android Studio
    Sync Gradle and download the neccessary Android sdk components

Features
--------

- Add Products to a Shopping Cart
- Create New Products with either custom or auto-generated Product Code
- View Shopping Cart Total Price

Overview
------------

Cash Register is designed with the Model-View-Presenter design pattern.  

- **Model**     Product
- **View**    ⋅  MainActivity (implements CashRegister.RegisterView)
- **Presenter**  MainPresenter (implements CashRegister.Presenter)

Model (entities Package)
----------
Product Class
- Properties (String Name, String ProductCode, BigDecimal Price)
- Parcelable


View
------------
MainActivity
- 2 Buttons (New Product and Checkout)
- 3 TextViews (2 Section Labels, 1 displays when Cart is empty)
- 2 RecyclerViews

Checkout Button
```
@Bind(R.id.main_checkout_button) Button CheckoutBtn;
```
- Visible When Items are in the Shopping Cart
- Launches CheckoutDialogFragment to show Total Price.

New Product Button
```
R.id.main_new_product_btn Button NewProduct;
```
- Launches AddProductDialogFragment to save a New Product

AllItemsRecycler

```
@Bind(R.id.main_all_products_recycler) RecyclerView AllItemsRecycler;
```

- Extends BaseProductRecyclerAdapter*
- Sets the Action "Add" Button Text and OnClickListeners
- Inner Interface ProductInteractionListener is passed in to constructor, Passes User events to Presenter.

CartRecycler 
```
@Bind(R.id.main_cart_recycler) RecyclerView CartRecycler;
```
- Extends BaseProductRecyclerAdapter*
- Sets the Action "Remove" Button Text and OnClickListeners
- Inner Interface CartInteractionListner is passed in to constructor, passes User events to Presenter.

*BaseProductRecyclerAdapter is an abstract class that extends RecyclerView.Adapter and sets a common view holder for its subclasses.

AddProductDialogFragment
- Shows a form for entering a new Product
- On Add the Presenter Validates Product Fields are Correct. 

CheckoutDialogFragment
- Displays the TotalPrice of Cart Items including Tax of 8.75%

Presenter
----------
MainPresenter 
- Implements CashRegister.Presenter Interface
- Holds references to a Shopping Cart and IProductRepository for data persistance
- Handle Business Logic - ex. creating a Product, adding & removing Product from the Cart, Bundles up state in onSaveInstanceState

Data Access Layer
----------
Responsible for persisting and retrieving data from the database
- **Database** FileDatabase Class which stores String versions of Products in a Text file on the device
- **Data Access** - IProductRepository must be implemented to Access data from the a Database.  ProductRepository implements the interface in the app.  For Unit Testing a Mock Implementation is created in its place.

Testing with Espresso
----------
Espresso Tests in MainActivityTest Class

- **setup** Resets the Database before every test.
- **setupClass** - instantiates the MainActivityViewActions reference
- **tearDown** - dereferences MainActivityViewActions for Garbage Collection

MainActivityViewActions
```
private static MainActivityViewActions Actions;
```
- Provides a Fluent API for interacting with MainActivity in a highly reuseable way.

Classes copied from external Github Repositories
- RecyclerViewMatcher
- TestUtils
- InnerViewAction

Unit Testing with JUnit
----------
Tests
- PresenterUnitTests
- UtilsTest

PresenterUnitTests
- Test Coverage for the CashRegister.Presenter implementations behaviors.
- The Property PresenterFixture should provide components to be tested. 
- The PresenterFixture currently has one component PresenterSUT that provides a fluent API to Test the CashRegister.Presenter implementations behavior.

Utils Test adds coverage of PriceFormatter Class.

Contribute
----------

- Issue Tracker: github.com/andreij6/CashRegister/issues
- Source Code: github.com/andreij6/CashRegister

Support
-------

If you are having issues, please let us know.
