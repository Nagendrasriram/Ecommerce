const BASE_URL = "http://localhost:8080/api";

/* 🔹 Helper function (handles errors properly) */
async function handleResponse(res) {
    if (!res.ok) {
        const text = await res.text();
        throw new Error(`API Error: ${text}`);
    }
    return res.json();
}

/* ================= PRODUCTS ================= */

export async function getProducts() {
    const res = await fetch(`${BASE_URL}/products`);
    return handleResponse(res);
}

/* ================= CART ================= */

export async function getCart() {
    const res = await fetch(`${BASE_URL}/cart?userid=1`);
    return handleResponse(res);
}

export async function addToCart(productId) {
    const res = await fetch(
        `${BASE_URL}/cart/add?userid=1&productId=${productId}&quantity=1`,
        { method: "POST" }
    );
    return handleResponse(res);
}

export async function decreaseItem(productId) {
    const res = await fetch(
        `${BASE_URL}/cart/decrease?userid=1&productId=${productId}`,
        { method: "POST" }
    );
    return handleResponse(res);
}

export async function removeItemApi(productId) {
    const res = await fetch(
        `${BASE_URL}/cart/remove?userid=1&productId=${productId}`,
        { method: "POST" }
    );
    return handleResponse(res);
}

/* ================= ORDER ================= */

export async function placeOrder() {
    const res = await fetch(
        `${BASE_URL}/orders/place?userid=1`,
        { method: "POST" }
    );
    return handleResponse(res);
}

export async function getOrders() {
    const res = await fetch(`${BASE_URL}/orders?userid=1`);
    return handleResponse(res);
}

export async function getOrderItems(orderId) {
    const res = await fetch(`${BASE_URL}/orders/${orderId}/items`);
    return handleResponse(res);
}