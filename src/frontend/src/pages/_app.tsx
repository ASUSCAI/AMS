// components
import Layout from "@/components/pages/Layout";

// contexts
import DateProvider from "@/contexts/DateContext";
import {DefaultTimeConfigProvider} from "@/contexts/DefaultTimeConfigContext";

// styles
import "@/styles/globals.css";

// types
import type { AppProps } from "next/app";

const App = ({ Component, pageProps }: AppProps) => {
  return (
    <DateProvider>
      <DefaultTimeConfigProvider>
        <Layout>
          <Component {...pageProps} />
        </Layout>
      </DefaultTimeConfigProvider>
    </DateProvider>
  );
};

export default App;
