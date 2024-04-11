import React, {
  useEffect,
  useState,
} from "react";

import {TimeConfig, areTimeConfigsEqual, BoundError} from "./ThresholdBox";
import DateSlider from "./DateSlider";

// styles
import axios from "axios";
import {Button, Checkbox} from "@instructure/ui";
import {useDefaultTimeConfig} from "@/contexts/DefaultTimeConfigContext";

interface SpecThresholdBoxProps {
  specificDate: Date;
  setSpecificDate: React.Dispatch<React.SetStateAction<Date>>;
}

const SpecThresholdBox: React.FC<SpecThresholdBoxProps> = ({specificDate}) => {
  const { defaultTimeConfigData } = useDefaultTimeConfig();
  const [timeConfigData, setTimeConfigData] = useState<TimeConfig>();
  const [inputConfigData, setInputConfigData] = useState<TimeConfig>();
  const [useDefault, setUseDefault] = useState(true);
  const [refreshSlider, doRefreshSlider] = useState(0);
  const [oneHandle, setOneHandle] = useState<boolean>(false);

  const courseID = 1234;

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setOneHandle(e.target.checked);
  };

  // ----------- fetch specific time Config -------------
  // fetch default and specific, if no specific, copy from default
  useEffect(() => {
    (async () => {

      const [date] = specificDate.toISOString().split("T");
      try {
        const res = await axios.get(`${process.env.NEXT_PUBLIC_URL}/timeConfig/${courseID}/${date}`);
        const config = res.data as TimeConfig;
        setTimeConfigData(config);
        setInputConfigData(config);
        setUseDefault(false);
      } catch (err) {
        console.error("No Specific TimeConfig:", err);
        setTimeConfigData(defaultTimeConfigData);
        setInputConfigData(defaultTimeConfigData);
        setUseDefault(true);
      }
    })();
  }, [specificDate, defaultTimeConfigData]);

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
  // retrieve time string from ISO Date string
  const retrieveTimeFromISODate = (dateVal: string | undefined): string => {
    if (!dateVal) {
      return "";
    } else {
      const formattedDate = new Date(dateVal).toLocaleString();

      const [date, time, meridian] = formattedDate.split(" ");
      let [hours, minutes] = time.split(":").map((p) => parseInt(p));
      if (meridian === "PM" && hours !== 12) {
        hours += 12;
      } else if (meridian === "AM" && hours === 12) {
        hours = 0;
      }

      return (
        hours.toString().padStart(2, "0") +
        ":" +
        minutes.toString().padStart(2, "0") +
        ":00"
      );
    }
  };

  const handleCancel = () => {
    setInputConfigData(timeConfigData);
    doRefreshSlider(prev => prev + 1);
  };
  const handleSave = () => {
    setTimeConfigData(inputConfigData);
    // const updateTimeConfig = async () => {
    //   const updatedConfig: TimeConfig = {
    //     id: inputConfigData ? inputConfigData.id : undefined,
    //     beginIn: inputConfigData
    //       ? retrieveTimeFromISODate(inputConfigData.beginIn)
    //       : undefined,
    //     endIn: inputConfigData
    //       ? retrieveTimeFromISODate(inputConfigData.endIn)
    //       : undefined,
    //     endLate: inputConfigData
    //       ? retrieveTimeFromISODate(inputConfigData.endLate)
    //       : undefined,
    //     beginOut: inputConfigData
    //       ? retrieveTimeFromISODate(inputConfigData.beginOut)
    //       : undefined,
    //     endOut: inputConfigData
    //       ? retrieveTimeFromISODate(inputConfigData.endOut)
    //       : undefined,
    //   };
    const updateTimeConfig = async () => {
      const updatedConfig: TimeConfig = {
        id: inputConfigData?.id, // Ensured to be not undefined by the check
        beginIn: inputConfigData?.beginIn,
        endIn: inputConfigData?.endIn,
        endLate: inputConfigData?.endLate,
        beginOut: inputConfigData?.beginOut,
        endOut: inputConfigData?.endOut,
      };
      const [date] = specificDate.toISOString().split("T");

      await axios
        .put(
          process.env.NEXT_PUBLIC_URL + "/timeConfig/" + courseID + "/" + date,
          updatedConfig
        )
        .then((response) => {
          console.log("Time Config updated:", response.data);
        })
        .catch((error) => {
          console.error("Error updating time config:", error);
        });
    };
    updateTimeConfig();
  };

  const handleDefaultToggle = async () => {
    setUseDefault((prevUseDefault) => {
      if (!prevUseDefault) {
        handleRevertDefault();
      } else {
        handleSave();
      }

      return !prevUseDefault;
    });
  };

  const handleRevertDefault = async () => {
    const [date] = specificDate.toISOString().split("T");
    setTimeConfigData(defaultTimeConfigData);
    setInputConfigData(defaultTimeConfigData);
    await axios
      .delete(process.env.NEXT_PUBLIC_URL + "/timeConfig/" + courseID + "/" + date)
      .then((response) => {
        doRefreshSlider(prev => prev + 1);
      })
      .catch((error) => {
        console.error("Error deleting time config:", error);
        doRefreshSlider(prev => prev + 1);
      });
  };

  return (
    <div>
      <DateSlider
        timeConfigData={timeConfigData}
        refresh={refreshSlider}
        disabled={useDefault}
        isOneHandleMode={oneHandle}
        onChange={(value: string[]) =>
          setInputConfigData((prevData: TimeConfig | undefined) => {
            if (prevData) {
              return {...prevData, endIn: value[0], endLate: value[1], beginOut: value[2]};
            }
            return prevData;
          })
        }
      />
      <div className="spec-btn-sect">
        <div className="use-default-checkbox">
          <Checkbox
            label="Default Config"
            checked={useDefault}
            size={"small"}
            onChange={handleDefaultToggle}
          />
          <Checkbox
            label="Assignment"
            checked={oneHandle}
            size={"small"}
            onChange={handleCheckboxChange}
          />
        </div>
        <div className="btn-box">
          <Button
            className="sub-btn cancel-btn"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
              transform: 'all 0.2s ease 0s',
            }}
            onClick={handleCancel}
            disabled={areTimeConfigsEqual(inputConfigData, timeConfigData)}
          >
            Cancel
          </Button>
          <Button
            className="sub-btn"
            color="primary"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
              transform: 'all 0.2s ease 0s',
            }}
            onClick={handleSave}
            disabled={areTimeConfigsEqual(inputConfigData, timeConfigData)}
          >
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SpecThresholdBox;
