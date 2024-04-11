import React, { createContext, useContext, useState, ReactNode, useCallback } from 'react';
import axios from "axios";

export type TimeConfig = {
  id: number | undefined;
  beginIn: string | undefined;
  endIn: string | undefined;
  endLate: string | undefined;
  beginOut: string | undefined;
  endOut: string | undefined;
};

interface DefaultTimeConfigContextType {
  defaultTimeConfigData: TimeConfig | undefined;
  setDefaultTimeConfigData: (config: TimeConfig | undefined) => void;
}

const DefaultTimeConfigContext = createContext<DefaultTimeConfigContextType | undefined>(undefined);

export const useDefaultTimeConfig = () => {
  const context = useContext(DefaultTimeConfigContext);
  if (context === undefined) {
    throw new Error('useTimeConfig must be used within a TimeConfigProvider');
  }
  return context;
};

export const DefaultTimeConfigProvider: React.FC<{children: ReactNode}> = ({ children }) => {
  const [defaultTimeConfigData, setDefaultTimeConfigData] = useState<TimeConfig | undefined>(undefined);

  // format time string to ISO Date string
  const formatTimeToISODate = (timeVal: string | undefined): string => {
    if (!timeVal) {
      return "";
    } else {
      const [hours, minutes] = timeVal?.split(":").map(Number);

      const date = new Date();
      date.setHours(hours, minutes, 0, 0);

      return date.toISOString();
    }
  };

  return (
    <DefaultTimeConfigContext.Provider value={{ defaultTimeConfigData, setDefaultTimeConfigData }}>
      {children}
    </DefaultTimeConfigContext.Provider>
  );
};