import React, {useEffect, useState,} from "react";
import ReactSlider from "react-slider";
import {TimeConfig} from "@/components/ThresholdBox";

function minutesToTime(minutes: number) {
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;

  const ampm = hours >= 12 ? "PM" : "AM";
  const formattedHours = hours % 12 === 0 ? 12 : hours % 12;

  return `${formattedHours}:${mins < 10 ? "0" : ""}${mins} ${ampm}`;
}

interface IntervalSliderProps {
  timeConfigData: TimeConfig | undefined;
  refresh: number;
  disabled: boolean;
  onChange: (value: string[]) => void;
  isOneHandleMode?: boolean;
}

const DateSlider: React.FC<IntervalSliderProps> = (props) => {
  const [day, setDay] = useState<Date>(new Date(0));
  const [value, setValue] = useState([0, 30, 60])
  const [beginIn, setBeginIn] = useState(0);
  const [endOut, setEndOut] = useState(60);
  const [hoveredTrack, setHoveredTrack] = useState<number | null>(null);
  const [activeThumb, setActiveThumb] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getTimeStamp = (time: String) => {
      let a = time.split(':');
      return (+a[0]) * 60 + (+a[1]);
    }
    if (props.timeConfigData &&
      props.timeConfigData.beginIn &&
      props.timeConfigData.endIn &&
      props.timeConfigData.endLate &&
      props.timeConfigData.beginOut &&
      props.timeConfigData.endOut) {
      setLoading(true);

      console.log("TimeConfigData");
      console.log(props.timeConfigData);

      const beginInTimestamp = getTimeStamp(props.timeConfigData.beginIn);
      const endOutTimestamp = getTimeStamp(props.timeConfigData.endOut);
      const valueTimeStamps = [
          getTimeStamp(props.timeConfigData.endIn),
          getTimeStamp(props.timeConfigData.endLate),
          getTimeStamp(props.timeConfigData.beginOut)
      ];

      console.log("valueTimeStamps");
      console.log(valueTimeStamps);

      setBeginIn(beginInTimestamp);
      setEndOut(endOutTimestamp);
      setLoading(false);
      onSliderChange(valueTimeStamps);
    }
  }, [props.timeConfigData, props.isOneHandleMode, props.refresh, loading]);

  const onSliderChange = (valueTimeStamps: number[]) => {
    if (loading) {
      return;
    }

    setValue(valueTimeStamps);

    let dateValue = [];

    for (let i = 0; i < valueTimeStamps.length; i++) {
      // dateValue.push((new Date(day.getTime() + valueTimeStamps[i] * 60000)).toISOString());
      const hours = Math.floor(valueTimeStamps[i] / 60) % 24; // Wrap around if hours >= 24
      const mins = valueTimeStamps[i] % 60;

      const formattedHours = hours.toString().padStart(2, '0');
      const formattedMinutes = mins.toString().padStart(2, '0');

      dateValue.push(`${formattedHours}:${formattedMinutes}:00`);
    }

    props.onChange(dateValue);
  }

  const Thumb: React.FC<any> = (props, state) => {
    return (
      <div
        {...props}
        onMouseDown={() => setActiveThumb(state.index)}
        onMouseOver={() => setActiveThumb(state.index)}
        onMouseLeave={() => setActiveThumb(null)}
      >
        <div className={`thumb-hover-area thumb-hover-area-${state.index}`}/>
        <div className="time-tooltip"><p>{minutesToTime(state.valueNow)}</p></div>
      </div>
    );
  }

  const Track: React.FC<any> = (props, state) => {
    const isHovered = (activeThumb === null && hoveredTrack === state.index) ||
      (activeThumb !== null && (activeThumb === state.index || activeThumb + 1 === state.index));
    return (
      <div
        {...props}
        onMouseEnter={() => setHoveredTrack(state.index)}
        onMouseLeave={() => setHoveredTrack(null)}
      >
        <div className={`track-hover-area track-hover-area-${state.index}`}/>
        <div className={`track-tooltip-container track-tooltip-container-${state.index} ${isHovered ? 'hovered' : ''}`}>
          <div className={"track-tooltip" + " track-tooltip-" + state.index}>
            <p>
              {state.index === 0 && "Arrived On Time"}
              {state.index === 1 && "Arrived Late"}
              {state.index === 2 && "Left Early"}
              {state.index === 3 && "Left On Time"}
            </p>
          </div>
        </div>
      </div>
    );
  }
  return (
    <div className={`slider-container ${props.disabled ? "disabled" : ""}`}>
      <ReactSlider
        className={"horizontal-slider" + (props.isOneHandleMode ? " one-handle" : "")}
        thumbClassName="thumb"
        trackClassName={"track" + (props.isOneHandleMode ? "-one-handle" : "")}
        value={props.isOneHandleMode ? [value[1]] : value}
        renderTrack={Track}
        renderThumb={Thumb}
        onChange={onSliderChange}
        minDistance={5}
        step={5}
        min={beginIn}
        max={endOut}
      />
    </div>
  );
}

export default DateSlider;