const BASE_URL = "http://localhost:8080/api";

export async function getProducts() {
    const response = await fetch(`${BASE_URL}/products`);
    if (!response.ok) {
        throw new Error("Failed to fetch products");
    }
    return response.json();
}