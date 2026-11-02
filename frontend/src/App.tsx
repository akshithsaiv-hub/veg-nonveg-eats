import { useEffect } from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import MobileCartBar from "@/components/MobileCartBar";
import Home from "@/pages/Home";
import Menu from "@/pages/Menu";
import CartPage from "@/pages/Cart";
import OrderConfirmation from "@/pages/OrderConfirmation";

function ScrollToTop() {
  const { pathname, search, hash } = useLocation();
  useEffect(() => {
    if (!hash) window.scrollTo({ top: 0 });
  }, [pathname, search, hash]);
  return null;
}

// One <Route> per page in src/pages; BrowserRouter already wraps this in main.tsx.
export default function App() {
  return (
    <div className="flex min-h-svh flex-col bg-background text-foreground">
      <ScrollToTop />
      <Navbar />
      <main className="flex-1">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/menu" element={<Menu />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/order/:orderId" element={<OrderConfirmation />} />
          <Route path="*" element={<Home />} />
        </Routes>
      </main>
      <Footer />
      <MobileCartBar />
      <Toaster />
    </div>
  );
}
