// components
import Layout from "@/components/pages/Layout";

// contexts
import DateProvider from "@/contexts/DateContext";

import '../styles/SectionConfigStyle.scss';

// types
import type { AppProps } from "next/app";

const App = ({ Component, pageProps }: AppProps) => {
  return (
    <DateProvider>
      <Layout>
        <Component {...pageProps} />
      </Layout>
    </DateProvider>
  );
};

export default App;
