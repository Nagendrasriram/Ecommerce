import { useEffect, useState } from "react";
import {
    getProducts,
    getCart,
    addToCart,
    decreaseItem,
    removeItemApi,
    placeOrder,
    getOrders,
    getOrderItems
} from "./services/api";

function App() {

    const [products, setProducts] = useState([]);
    const [cart, setCart] = useState([]);
    const [orders, setOrders] = useState([]);
    const [orderItems, setOrderItems] = useState({});
    const [view, setView] = useState("products");
    const [loading, setLoading] = useState(false);

    // 🔹 Load products
    useEffect(() => {
        getProducts().then(data => setProducts(data));
    }, []);

    // 🔹 Load cart
    const loadCart = () => {
        getCart().then(data => {
            if (data && data.data) {
                setCart(data.data);
            } else {
                setCart([]);
            }
        });
    };

    useEffect(() => {
        loadCart();
    }, []);

    // 🔹 Product map
    const productMap = {};
    products.forEach(p => {
        productMap[p.id] = p;
    });

    // 🔹 Orders
    const loadOrders = () => {
        getOrders().then(data => setOrders(data));
    };

    const loadOrderItems = async (orderId) => {
        const items = await getOrderItems(orderId);
        setOrderItems(prev => ({ ...prev, [orderId]: items }));
    };

    // 🔹 Cart actions
    const handleAddToCart = async (productId) => {
        await addToCart(productId);
        loadCart();
    };

    const updateQuantity = async (productId, change) => {
        if (change === 1) {
            await addToCart(productId);
        } else {
            await decreaseItem(productId);
        }
        loadCart();
    };

    const removeItem = async (productId) => {
        await removeItemApi(productId);
        loadCart();
    };

    const handlePlaceOrder = async () => {
        setLoading(true);
        await placeOrder();
        setLoading(false);
        alert("Order placed!");
        loadCart();
    };

    return (
        <div style={{ padding: "20px", background: "#f5f5f5", minHeight: "100vh" }}>

            <h1>🛒 SmartCart</h1>

            {/* NAVIGATION */}
            <div style={{ marginBottom: "20px" }}>
                <button onClick={() => setView("products")}>Products</button>
                <button onClick={() => setView("cart")} style={{ marginLeft: "10px" }}>Cart</button>
                <button onClick={() => { setView("orders"); loadOrders(); }} style={{ marginLeft: "10px" }}>
                    Orders
                </button>
            </div>

            {/* PRODUCTS */}
            {view === "products" && (
                <>
                    <h2>Products</h2>

                    {products.length === 0 && <p>Loading...</p>}

                    {products.map(product => (
                        <div key={product.id} style={{
                            border: "1px solid #ddd",
                            borderRadius: "10px",
                            margin: "10px",
                            padding: "15px",
                            background: "#fff",
                            boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
                        }}>
                            <h3>{product.name}</h3>
                            <p>{product.description}</p>
                            <p>₹{product.price}</p>

                            <button
                                onClick={() => handleAddToCart(product.id)}
                                style={{
                                    padding: "8px 12px",
                                    borderRadius: "5px",
                                    background: "#007bff",
                                    color: "white",
                                    border: "none"
                                }}
                            >
                                Add to Cart
                            </button>
                        </div>
                    ))}
                </>
            )}

            {/* CART */}
            {view === "cart" && (
                <>
                    <h2>Cart</h2>

                    {cart.length === 0 && <p>Your cart is empty 🛒</p>}

                    {cart.map(item => {
                        const product = productMap[item.productId];
                        if (!product) return null;

                        return (
                            <div key={item.id} style={{
                                border: "1px solid #ddd",
                                borderRadius: "10px",
                                margin: "10px",
                                padding: "15px",
                                background: "#fff"
                            }}>
                                <h4>{product.name}</h4>
                                <p>₹{product.price}</p>

                                <div>
                                    <button onClick={() => updateQuantity(item.productId, -1)}>-</button>
                                    <span style={{ margin: "0 10px" }}>{item.quantity}</span>
                                    <button onClick={() => updateQuantity(item.productId, 1)}>+</button>
                                </div>

                                <p>Total: ₹{product.price * item.quantity}</p>

                                <button
                                    onClick={() => removeItem(item.productId)}
                                    style={{ background: "red", color: "white", marginTop: "5px" }}
                                >
                                    Remove
                                </button>
                            </div>
                        );
                    })}

                    <h3>
                        Total: ₹{
                        cart.reduce((sum, item) => {
                            const product = productMap[item.productId];
                            return sum + (product ? product.price * item.quantity : 0);
                        }, 0)
                    }
                    </h3>

                    {cart.length > 0 && (
                        <button onClick={handlePlaceOrder} disabled={loading}>
                            {loading ? "Placing..." : "Place Order"}
                        </button>
                    )}
                </>
            )}

            {/* ORDERS */}
            {view === "orders" && (
                <>
                    <h2>Orders</h2>

                    {orders.length === 0 && <p>No orders yet</p>}

                    {orders.map(order => (
                        <div key={order.id} style={{
                            border: "1px solid #aaa",
                            margin: "10px",
                            padding: "10px",
                            background: "#fff"
                        }}>
                            <h4>Order #{order.id}</h4>
                            <p>Total: ₹{order.totalAmount}</p>

                            <button onClick={() => loadOrderItems(order.id)}>
                                View Items
                            </button>

                            {orderItems[order.id]?.map(item => {
                                const product = productMap[item.productId];
                                return (
                                    <div key={item.id}>
                                        {product?.name} - Qty: {item.quantity}
                                    </div>
                                );
                            })}
                        </div>
                    ))}
                </>
            )}

        </div>
    );
}

export default App;