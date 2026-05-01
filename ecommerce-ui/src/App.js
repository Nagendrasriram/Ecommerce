import { useEffect, useState } from "react";
import { getProducts } from "./services/api";

function App() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getProducts()
        .then(data => setProducts(data))
        .catch(err => console.error(err));
  }, []);

  return (
      <div style={{ padding: "20px" }}>
        <h1>🛒 My E-Commerce App</h1>

        <h2>Products</h2>

        {products.map(product => (
            <div key={product.id} style={{border:"1px solid #ccc", padding:"10px", margin:"10px"}}>
              <h3>{product.name}</h3>
              <p>{product.description}</p>
              <p>Price: ₹{product.price}</p>
              <p>Stock: {product.stock}</p>
            </div>
        ))}
      </div>
  );
}

export default App;