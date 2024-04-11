import React, { Component } from 'react';
import { Button, CloseButton, Flex, Tray, View } from '@instructure/ui';
import ThresholdBox from './ThresholdBox';
import SpecConfigBox from './SpecConfigBox';
import SectionConfig from './SectionConfig';

interface ConfigTrayProps {}

interface ConfigTrayState {
  open: boolean;
}

export class ConfigTray extends Component<ConfigTrayProps, ConfigTrayState> {
  private trayRef: HTMLDivElement | null = null;

  constructor(props: ConfigTrayProps) {
    super(props);
    this.state = {
      open: false,
    };
  }

  // componentDidMount() {
  //   document.addEventListener('mousedown', this.handleClickOutside);
  // }
  //
  // componentWillUnmount() {
  //   document.removeEventListener('mousedown', this.handleClickOutside);
  // }

  hideTray = () => {
    this.setState({
      open: false,
    });
  };

  handleClickOutside = (event: MouseEvent) => {
    if (this.trayRef && !this.trayRef.contains(event.target as Node)) {
      this.hideTray();
    }
  };

  renderCloseButton() {
    return (
      <Flex>
        <Flex.Item shouldGrow shouldShrink>
          <div className="expand-label" style={{ display: 'inline-block' }}>Date Specific Time Config</div>
        </Flex.Item>
        <Flex.Item>
          <CloseButton
            placement="end"
            offset="small"
            screenReaderLabel="Close"
            onClick={this.hideTray}
          />
        </Flex.Item>
      </Flex>
    );
  }

  render() {
    return (
      <div className="time-config-btn" ref={node => this.trayRef = node}>
        <Button
          onClick={() => {
            this.setState({ open: true });
          }}
        >
          Show Time Config
        </Button>
        <Tray
          label="Tray Example"
          open={this.state.open}
          onDismiss={this.hideTray}
          shouldCloseOnDocumentClick={false}
          transitionExit={true}
          size="regular"
          placement="end"
        >
          <View as="div" padding="medium">
            {this.renderCloseButton()}
            <SpecConfigBox />
            {/*<ThresholdBox/>*/}
            <SectionConfig />
          </View>
        </Tray>
      </div>
    );
  }
}